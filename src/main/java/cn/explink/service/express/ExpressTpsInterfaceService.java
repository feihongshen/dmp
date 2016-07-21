package cn.explink.service.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.express.ExpressTpsInterfaceExcepRecordDAO;
import cn.explink.dao.express.GeneralDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.express.ExpressTpsInterfaceExcepRecord;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.Tools;
import cn.explink.util.express.ExpressTpsInterfaceException;

import com.pjbest.deliveryorder.bizservice.PjDeliverOrder4DMPRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPResponse;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderResponse;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryTrackInfo;
import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageModel;
import com.pjbest.deliveryorder.dcpackage.service.PjUnPackRequest;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;
import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;
import com.vip.top.mainid.bizservice.MainIdService;
import com.vip.top.mainid.bizservice.MainIdServiceHelper;

/**
 * JMS快递业务接口调用监听类
 * @author jiangyu 2015年9月7日
 *
 */
@Component
public class ExpressTpsInterfaceService implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CamelContext camelContext;

	@Autowired
	ExpressTpsInterfaceExcepRecordDAO expressTpsInterfaceExcepRecordDAO;

	@Autowired
	private GeneralDAO generalDAO;

	ObjectMapper objectMapper = JacksonMapper.getInstance();
	ObjectReader expressOperationInfoReader = this.objectMapper.reader(ExpressOperationInfo.class);
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_EXECUTE_TPS_INTERFACE = "jms:queue:VirtualTopicConsumers.tps.executeTpsInterface";

	public void init() {
		this.logger.info("init tps interface camel routes");
		try {
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI_EXECUTE_TPS_INTERFACE + "?concurrentConsumers=15").to("bean:expressTpsInterfaceService?method=exeTpsInterface")
							.routeId("TPS-IntefaceMatch");
				}
			});
		} catch (Exception e) {
			this.logger.error("camel context start fail", e);
		}

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.init();
	}

	public Map<String, Object> exeTpsInterface(@Header("executeTpsInterfaceHeader") String param, @Header("MessageHeaderUUID") String messageHeaderUUID) throws Exception {
		this.logger.info("dmp execute tps interface  调用环节信息处理,{}", param);
		try{
			ExpressOperationInfo operationInfo = this.expressOperationInfoReader.readValue(param);
			Map<String, Object> resultMap = this.tpsInterfaceHandlePorcess(operationInfo);
			return resultMap;
		} catch (Exception e) {
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".exeTpsInterface")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_EXECUTE_TPS_INTERFACE)
					.buildMessageHeader("executeTpsInterfaceHeader", param)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
		return null;
		
	}

	/**
	 * 根据不同的操作环节选择适合的接口
	 * @param operationInfo
	 * @return
	 */
	public Map<String, Object> tpsInterfaceHandlePorcess(ExpressOperationInfo operationInfo) {
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		//存放返回结果
		resultMap.clear();
		if (operationInfo != null) {
			//首先创建一条记录
			Long recordId = this.createExceptionLog(operationInfo);
			try {
				//根据不同的业务场景调用不同的服务
				InvocationContext.Factory.getInstance().setTimeout(10000);
				if (ExpressOperationEnum.PreOrderFeedBack.getValue().equals(operationInfo.getOperationType())) {
					//揽件反馈接口
					Boolean result = this.preOrderFeedBackProcess(operationInfo.getPreOrderfeedBack());
					resultMap.put(ExpressOperationEnum.PreOrderFeedBack.getUniqueCode(), result);
					this.logger.info("调用TPS接口正常，反馈操作的结果回传到Tps后的结果为：" + result);

				} else if (ExpressOperationEnum.PreOrderQuery.getValue().equals(operationInfo.getOperationType())) {
					//查询预约单轨迹
					List<PjDeliveryTrackInfo> queryResult = this.getPreOrderQureyResult(operationInfo.getReserveOrderNo());
					resultMap.put(ExpressOperationEnum.PreOrderQuery.getUniqueCode(), queryResult);
					this.logger.info("调用TPS接口正常，查询预约单轨迹请求Tps后的结果为：{}", queryResult);

				} else if (ExpressOperationEnum.CreateTransNO.getValue().equals(operationInfo.getOperationType())) {
					//创建运单接口
					List<PjDeliveryOrderResponse> result = this.createTransNo4Dmp(operationInfo.getRequestlist());
					resultMap.put(ExpressOperationEnum.CreateTransNO.getUniqueCode(), result);
					this.logger.info("调用TPS接口正常，创建运单接口请求Tps后的结果为：{}", result);

				} else if (ExpressOperationEnum.TransNOFeedBack.getValue().equals(operationInfo.getOperationType())) {
					//运单状态反馈接口
					Boolean resultFlag = this.transNoFeedBackProcess(operationInfo.getTransNoFeedBack());
					resultMap.put(ExpressOperationEnum.TransNOFeedBack.getUniqueCode(), resultFlag);
					this.logger.info("调用TPS接口正常，运单状态反馈接口请求Tps后的结果为：{}", resultFlag);

				} else if (ExpressOperationEnum.TransNOTraceQuery.getValue().equals(operationInfo.getOperationType())) {
					//运单轨迹查询接口
					List<PjDeliveryTrackInfo> result = this.queryTransNoTrace(operationInfo.getTransNo());
					resultMap.put(ExpressOperationEnum.TransNOTraceQuery.getUniqueCode(), result);
					this.logger.info("调用TPS接口正常，运单轨迹查询接口请求Tps后的结果为：{}", result);

				} else if (ExpressOperationEnum.PackOpereate.getValue().equals(operationInfo.getOperationType())) {
					//上传打包信息接口
					Boolean resultFlag = this.handlePackingInfo(operationInfo.getPackModel());
					resultMap.put(ExpressOperationEnum.PackOpereate.getUniqueCode(), resultFlag);
					this.logger.info("调用TPS接口正常，上传打包信息接口请求Tps后的结果为：{}", resultFlag);

				} else if (ExpressOperationEnum.UnPackOperate.getValue().equals(operationInfo.getOperationType())) {
					//上传拆包信息接口
					Boolean resultFlag = this.handleUnPackingInfo(operationInfo.getUnPackRequest());
					resultMap.put(ExpressOperationEnum.UnPackOperate.getUniqueCode(), resultFlag);
					this.logger.info("调用TPS接口正常，上传拆包信息接口请求Tps后的结果为：{}", resultFlag);

				} else if (ExpressOperationEnum.VerifyTransNo.getValue().equals(operationInfo.getOperationType())) {
					//检测运单是否属于包接口
					Boolean resultFlag = this.checkTransNoBelongPackage(operationInfo.getTransNo(), operationInfo.getPackageNo());
					resultMap.put(ExpressOperationEnum.VerifyTransNo.getUniqueCode(), resultFlag);
					this.logger.info("调用TPS接口正常，检测运单是否属于包接口请求Tps后的结果为：{}", resultFlag);

				} else if (ExpressOperationEnum.DMPCarrierHandOver.getValue().equals(operationInfo.getOperationType())) {
					//落地配承运商下发接口
					/*
					List<PjDeliverOrder4DMPRequest> result = this.handOverDmpCarrier(operationInfo.getCarrierCode());
					resultMap.put(ExpressOperationEnum.DMPCarrierHandOver.getUniqueCode(), result);
					this.logger.info("调用TPS接口正常，落地配承运商下发接口请求Tps后的结果为：{}", result);
					*/

				} else if (ExpressOperationEnum.SnifferTransNoFeedBack.getValue().equals(operationInfo.getOperationType())) {
					//落地配抓取运单反馈接口
					/*
					this.snifferTransNoFeedBack(operationInfo.getTransNoIdList());
					resultMap.put(ExpressOperationEnum.SnifferTransNoFeedBack.getUniqueCode(), true);
					this.logger.info("调用TPS接口正常，落地配抓取运单反馈接口请求Tps成功");
					*/

				} else if (ExpressOperationEnum.SnifferPerOrderNoFeedBack.getValue().equals(operationInfo.getOperationType())) {
					//落地配抓取预约单反馈接口
					this.snifferPreOrderNoFeedBack(operationInfo.getListStrNo());
					resultMap.put(ExpressOperationEnum.SnifferPerOrderNoFeedBack.getUniqueCode(), true);
					this.logger.info("调用TPS接口正常，落地配抓取预约单反馈接口请求Tps成功");

				} else if (ExpressOperationEnum.MainTransNoGenerate.getValue().equals(operationInfo.getOperationType())) {
					//主单号生成接口
					String result = this.generateMainTransNo(operationInfo.getSeqRuleNo(), operationInfo.getContextVars());
					resultMap.put(ExpressOperationEnum.MainTransNoGenerate.getUniqueCode(), result);
					this.logger.info("调用TPS接口正常，主单号生成接口请求Tps成功");
				}
			} catch (ExpressTpsInterfaceException e) {
				//调用Tps接口出现的异常
				this.logger.info(e.getMessage());
				this.logger.error(e.getMessage());
				this.updateExceptionLog(recordId, e.getMessage());
			} catch (Exception e) {
				//组装JMS监听方法出现异常
				this.logger.info("系统异常，请联系管理员，异常原因：{}", e.getMessage());
				this.updateExceptionLog(recordId, e.getMessage());
			}
		}
		return resultMap;
	}

	/**
	 * 主单号生成接口
	 * @param seqRuleNo 运单号或包号规则代码
	 * @param contextVars 运单号或包号规则参数
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public String generateMainTransNo(String seqRuleNo, Map<String, String> contextVars) throws ExpressTpsInterfaceException {
		String result = "";
		MainIdService mainIdService = new MainIdServiceHelper.MainIdServiceClient();
		try {
			result = mainIdService.getNextSeq(seqRuleNo, contextVars);
		} catch (OspException e) {
			this.logger.info("请求TPS主单号生成接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return result;
	}

	/**
	 * 落地配抓取预约单反馈接口
	 * @param listStrNo
	 * @throws ExpressTpsInterfaceException
	 */
	public void snifferPreOrderNoFeedBack(List<String> listStrNo) throws ExpressTpsInterfaceException {
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			pjDeliveryOrderService.makeSaleOrderSuccessList(listStrNo);
		} catch (OspException e) {
			this.logger.info("请求TPS落地配抓取运单反馈接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
	}

	/**
	 * 落地配抓取运单反馈接口
	 * @param listStrNo 预约单号集合
	 * @throws ExpressTpsInterfaceException
	 */
	public void snifferTransNoFeedBack(List<String> listStrNo) throws ExpressTpsInterfaceException {
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		try {
			pjDeliveryOrder4DMPService.makeDeliveryOrderSuccessList(listStrNo);
		} catch (OspException e) {
			this.logger.info("请求TPS落地配抓取运单反馈接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
	}

	/**
	 * 检测运单是否属于包接口
	 * @param transNo 运单号
	 * @param packageNo 包号
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public Boolean checkTransNoBelongPackage(String transNo, String packageNo) throws ExpressTpsInterfaceException {
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			resultFlag = pjDeliveryOrderService.checkPackageInfo(transNo, packageNo);
		} catch (OspException e) {
			this.logger.info("请求TPS检测运单是否属于包接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultFlag;
	}

	/**
	 * 落地配承运商下发接口
	 * @param transNo 运单号
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public List<PjDeliverOrder4DMPRequest> handOverDmpCarrier(String carrierCode) throws ExpressTpsInterfaceException {
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		List<PjDeliverOrder4DMPRequest> result = new ArrayList<PjDeliverOrder4DMPRequest>();
		try {
			result = pjDeliveryOrder4DMPService.getDeliveryOrder(carrierCode);
		} catch (OspException e) {
			this.logger.info("请求TPS落地配承运商下发接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return result;
	}

	/**
	 * 上传拆包信息接口
	 * @param packModel
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public Boolean handleUnPackingInfo(PjUnPackRequest unPackRequest) throws ExpressTpsInterfaceException {
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			if (unPackRequest != null) {
				resultFlag = pjDeliveryOrderService.unPack(unPackRequest);
			}
			if (resultFlag == true) {
				this.logger.info("请求TPS上传拆包信息成功");
			} else {
				this.logger.info("请求TPS上传拆包信息失败");
				throw new ExpressTpsInterfaceException("请求TPS上传拆包信息失败");
			}
		} catch (OspException e) {
			this.logger.info("请求TPS上传拆包信息接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultFlag;
	}

	/**
	 * 上传打包信息接口
	 * @param packModel
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public Boolean handlePackingInfo(PjDcPackageModel packModel) throws ExpressTpsInterfaceException {
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			resultFlag = pjDeliveryOrderService.upLoadPackageInfo(packModel);
		} catch (OspException e) {
			this.logger.info("请求TPS上传打包信息接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultFlag;
	}

	/**
	 * 运单轨迹查询接口
	 * @param transNo
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public List<PjDeliveryTrackInfo> queryTransNoTrace(String transNo) throws ExpressTpsInterfaceException {
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		List<PjDeliveryTrackInfo> result = new ArrayList<PjDeliveryTrackInfo>();
		try {
			result = pjDeliveryOrderService.getDeliveryOrderTracking(transNo);
		} catch (OspException e) {
			this.logger.info("请求TPS运单状态反馈接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return result;
	}

	/**
	 * 运单状态反馈接口
	 * @param transNoFeedBack
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public Boolean transNoFeedBackProcess(PjTransportFeedbackRequest transNoFeedBack) throws ExpressTpsInterfaceException {
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			resultFlag = pjDeliveryOrderService.feedbackTransport(transNoFeedBack);
			if (!resultFlag) {
				this.logger.info("请求TPS验证不通过,返回false!");
				throw new ExpressTpsInterfaceException("请求TPS返回false");
			}
		} catch (OspException e) {
			this.logger.info("请求TPS运单状态反馈接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultFlag;
	}

	/**
	 * 创建运单接口
	 * @param requestlist
	 * @description 补录的时候，把信息回写到dmp
	 * @author 江宇、刘武强
	 * @date  2015年9月1日下午1:54:03
	 * @param  @param requestlist 需要回写的信息
	 * @return  void
	 * @throws
	 * @throws ExpressTpsInterfaceException
	 */
	public List<PjDeliveryOrderResponse> createTransNo4Dmp(List<PjDeliveryOrderRequest> requestlist) throws ExpressTpsInterfaceException {
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		List<PjDeliveryOrderResponse> resultlist = new ArrayList<PjDeliveryOrderResponse>();
		PjDeliveryOrderResponse result = new PjDeliveryOrderResponse();;
		InvocationContext.Factory.getInstance().setTimeout(10000);//10s内没完成，抛出延时异常
		try {
			for(int i=0;i<requestlist.size();i++){
				result = pjDeliveryOrderService.inputDeliveryOrder(requestlist.get(i));
				resultlist.add(result);
			}
			
			if ((resultlist.size() <= 0) || (resultlist.size() > 1)) {
				this.logger.info("没有返回信息或者返回信息大于1条");
				//调用失败信息存入数据库
				throw new ExpressTpsInterfaceException("没有返回信息或者返回信息大于1条");
			} else if (resultlist.size() == 1) {
				if ((resultlist.get(0).getResultCode() != null) && "1".equals(resultlist.get(0).getResultCode())) {
					this.logger.info("调用成功");
				} else {
					this.logger.info("调用失败");
					if (resultlist.get(0).getResultMsg() != null) {
						this.logger.info(resultlist.get(0).getResultMsg());
						//调用失败信息存入数据库
						throw new ExpressTpsInterfaceException(resultlist.get(0).getResultMsg());
					}
				}
			}
		} catch (OspException e) {
			this.logger.info("请求TPS创建运单接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultlist;
	}

	/**
	 * 查询预约单轨迹
	 * @param reserveOrderNo
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public List<PjDeliveryTrackInfo> getPreOrderQureyResult(String reserveOrderNo) throws ExpressTpsInterfaceException {
		List<PjDeliveryTrackInfo> pjDeliveryTrackInfo = new ArrayList<PjDeliveryTrackInfo>();
		try {
			if (!Tools.isEmpty(reserveOrderNo)) {
				PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
				pjDeliveryTrackInfo = pjDeliveryOrderService.findReserveTracks(reserveOrderNo);
			} else {
				this.logger.info("预订单号：{}", "null");
			}
		} catch (OspException e) {
			this.logger.info("预订单号：{} ,查询预约单轨迹TPS接口异常,异常原因为{}", reserveOrderNo, e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return pjDeliveryTrackInfo;
	}

	/**
	 * 预订单的揽件反馈
	 * @param operationInfo
	 * @return
	 * @throws ExpressTpsInterfaceException
	 */
	public Boolean preOrderFeedBackProcess(PjSaleOrderFeedbackRequest request) throws ExpressTpsInterfaceException {
		Boolean resultFlag = false;
		try {
			//揽件反馈
			if (request != null) {
				PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
				resultFlag = pjDeliveryOrderService.feedbackSaleOrder(request);
				if (!resultFlag) {//返回值为false  创建异常记录
					throw new ExpressTpsInterfaceException("调用将反馈结果返回给TPS接口提示结果失败");
				}
			}
		} catch (OspException e) {
			this.logger.info("预订单：" + request.getReserveOrderNo() + "将反馈结果返回给TPS接口异常,异常原因为{}", e.getMessage());
			throw new ExpressTpsInterfaceException(e.getMessage());
		}
		return resultFlag;
	}

	/**
	 * 创建操作记录
	 * @param obj
	 */
	@Transactional
	private Long createExceptionLog(ExpressOperationInfo params) {
		ExpressTpsInterfaceExcepRecord entity = new ExpressTpsInterfaceExcepRecord();
		entity.setPreOrderNo(params.getReserveOrderNo());
		try {
			this.setTransNo(params, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setPackageNo(params.getPackageNo());
		entity.setErrMsg("");
		entity.setExecuteCount(0);
		entity.setMethodParams(Tools.obj2json(params));
		entity.setOpeFlag(YesOrNoStateEnum.Yes.getValue());//是否执行成功了
		entity.setOperationType(params.getOperationType());//对应接口枚举ExpressOperationEnum
		//操作环节【现在默认使用这个，后期替换】
		entity.setFlowOrderType(0L);
		//接口名称
		entity.setRemark(ExpressOperationEnum.getByValue(params.getOperationType()).getText());

		Long id = this.expressTpsInterfaceExcepRecordDAO.createTpsInterfaceExcepRecord(entity);
		if (id.intValue() > 0) {
			this.logger.debug("调用Tps接口记录创建成功");
		}
		return id;
	}

	/**
	 * 异常记录
	 * @param obj
	 */
	@Transactional
	public Long updateExceptionLog(Long recordId, String exceptionMsg) {
		Long id = this.expressTpsInterfaceExcepRecordDAO.updateTpsInterfaceExcepRecord(recordId, exceptionMsg);
		if (id.intValue() > 0) {
			this.logger.debug("调用Tps异常记录异常信息成功");
		}
		return id;
	}

	public void setTransNo(ExpressOperationInfo operationInfo, ExpressTpsInterfaceExcepRecord entity) {
		if (ExpressOperationEnum.CreateTransNO.getValue().equals(operationInfo.getOperationType())) {
			//创建运单接口
			entity.setTransNo(operationInfo.getRequestlist().get(0).getTransportNo());
		} else if (ExpressOperationEnum.PreOrderFeedBack.getValue().equals(operationInfo.getOperationType())) {
			//揽件反馈接口
			entity.setTransNo(operationInfo.getPreOrderfeedBack().getTransportNo());
		} else if (ExpressOperationEnum.PreOrderQuery.getValue().equals(operationInfo.getOperationType())) {
			//查询预约单轨迹
			entity.setTransNo(operationInfo.getReserveOrderNo());
		} else if (ExpressOperationEnum.TransNOFeedBack.getValue().equals(operationInfo.getOperationType())) {
			//运单状态反馈接口
			entity.setTransNo(operationInfo.getTransNoFeedBack().getTransportNo());
		} else if (ExpressOperationEnum.TransNOTraceQuery.getValue().equals(operationInfo.getOperationType())) {
			//运单轨迹查询接口
			entity.setTransNo(operationInfo.getTransNo());
		} else if (ExpressOperationEnum.PackOpereate.getValue().equals(operationInfo.getOperationType())) {
			//上传打包信息接口
			entity.setTransNo(operationInfo.getTransNo());
		} else if (ExpressOperationEnum.UnPackOperate.getValue().equals(operationInfo.getOperationType())) {
			//上传拆包信息接口
			entity.setTransNo(operationInfo.getUnPackRequest().getTransportNo());
		} else if (ExpressOperationEnum.VerifyTransNo.getValue().equals(operationInfo.getOperationType())) {
			//检测运单是否属于包接口
			entity.setTransNo(operationInfo.getTransNo());
		} else if (ExpressOperationEnum.SnifferPerOrderNoFeedBack.getValue().equals(operationInfo.getOperationType())) {
			//落地配抓取预约单反馈接口
			entity.setTransNo(operationInfo.getTransNo());
		} else if (ExpressOperationEnum.MainTransNoGenerate.getValue().equals(operationInfo.getOperationType())) {
			//主单号生成接口
			entity.setTransNo(operationInfo.getTransNo());
		} else {
			entity.setTransNo(operationInfo.getTransNo());
		}
	}

}
