package cn.explink.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbOrderTailDao;
import cn.explink.dao.CwbTiHuoDAO;
import cn.explink.dao.DeleteCwbDAO;
import cn.explink.dao.DeliveryChukuDAO;
import cn.explink.dao.DeliveryDaohuoDAO;
import cn.explink.dao.DeliveryJuShouDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.DeliverySuccessfulDAO;
import cn.explink.dao.DeliveryZhiLiuDAO;
import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.KDKDeliveryChukuDAO;
import cn.explink.dao.KuFangRuKuDao;
import cn.explink.dao.KuFangZaiTuDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.TuiHuoChuZhanDao;
import cn.explink.dao.TuiHuoZhanRuKuDao;
import cn.explink.dao.UpdateMessageDAO;
import cn.explink.dao.ZhongZhuanDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbTiHuo;
import cn.explink.domain.DeliveryChuku;
import cn.explink.domain.DeliveryDaohuo;
import cn.explink.domain.DeliveryJuShou;
import cn.explink.domain.DeliverySuccessful;
import cn.explink.domain.DeliveryZhiLiu;
import cn.explink.domain.ExpressSysMonitor;
import cn.explink.domain.KDKDeliveryChuku;
import cn.explink.domain.KuFangRuKuOrder;
import cn.explink.domain.KuFangZaiTuOrder;
import cn.explink.domain.TuiHuoChuZhanOrder;
import cn.explink.domain.TuiHuoZhanRuKuOrder;
import cn.explink.domain.UpdateMessage;
import cn.explink.domain.ZhongZhuan;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.ChangeFunctionidEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.FlowTimeEnum;
import cn.explink.enumutil.UpdateMessageMenuNameEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class FlowFromJMSService {
	@Autowired
	private CamelContext camelContext;
	@Autowired
	private OrderFlowDAO orderFlowDAO;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	DeliverySuccessfulDAO deliverySuccessfulDAO;
	@Autowired
	DeliveryChukuDAO deliveryChukuDAO;
	@Autowired
	DeliveryZhiLiuDAO deliveryZhiLiuDAO;
	@Autowired
	DeliveryDaohuoDAO deliveryDaohuoDAO;
	@Autowired
	UpdateMessageDAO updateMessageDAO;
	@Autowired
	ZhongZhuanDAO zhongZhuanDAO;
	@Autowired
	GetDmpDAO getDmpDAO;

	@Autowired
	DeliveryJuShouDAO deliveryJuShouDAO;
	@Autowired
	CwbTiHuoDAO cwbTiHuoDAO;
	@Autowired
	TuiHuoChuZhanDao tuiHuoChuZhanDao;
	@Autowired
	DeleteCwbDAO deleteCwbDAO;
	@Autowired
	KDKDeliveryChukuDAO kdkDeliveryChukuDAO;
	@Autowired
	TuiHuoZhanRuKuDao tuiHuoZhanRuKuDao;
	@Autowired
	KuFangZaiTuDao kuFangZaiTuDao;
	@Autowired
	KuFangRuKuDao kuFangRuKuDao;
	@Autowired
	CwbOrderTailService cwbOrderTailService;
	@Autowired
	CwbOrderTailDao cwbOrderTailDao;
	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectReader dmpOrderFlowReader = objectMapper.reader(DmpOrderFlow.class);
	ObjectReader cwbOrderWithDeliveryStateReader = objectMapper.reader(CwbOrderWithDeliveryState.class);

	private Logger logger = LoggerFactory.getLogger(FlowFromJMSService.class);

	@PostConstruct
	public void init() throws Exception {
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("jms:queue:VirtualTopicConsumers.oms1.orderFlow?concurrentConsumers=10").to("bean:flowFromJMSService?method=saveFlow").routeId("floworderRoute");
				from("jms:queue:VirtualTopicConsumers.oms1.losecwb?concurrentConsumers=10").to("bean:flowFromJMSService?method=deletecwb").routeId("deletecwb");
				from("jms:queue:VirtualTopicConsumers.oms1.losecwbbatch?concurrentConsumers=10").to("bean:flowFromJMSService?method=deletecwbBatch").routeId("losecwbAll");
			}
		});
	}

	public void deletecwb(@Header("cwbAndUserid") String cwbAndUserid) {
		String cwb = cwbAndUserid.split(",")[0];
		logger.info("数据删除功能：操作人：{}，订单号：{}", cwbAndUserid.split(",")[1], cwb);
		try {
			if (cwb.trim().length() > 0) {
				deleteCwbDAO.deleteB2cdataLiantongByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchHappygoByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchSaohuobangByCwb(cwb);
				deleteCwbDAO.deleteCwbOrderByCwb(cwb);
				deleteCwbDAO.deleteDeliveryChukuByCwb(cwb);
				deleteCwbDAO.deleteDeliveryDaohuoByOpscwbid(cwb);
				deleteCwbDAO.deleteDeliveryJushouByCwb(cwb);
				deleteCwbDAO.deleteDeliveryStateByCwb(cwb);
				deleteCwbDAO.deleteDeliverySuccessfulByCwb(cwb);
				deleteCwbDAO.deleteDeliveryZhiliuByCwb(cwb);
				deleteCwbDAO.deleteSendB2CCodByCwb(cwb);
				deleteCwbDAO.deleteSendB2CDataByCwb(cwb);
				deleteCwbDAO.deleteTiHuoByCwb(cwb);
				deleteCwbDAO.deleteZhongZhuanByCwb(cwb);
				deleteCwbDAO.deleteTuiHuoZhanRuKu(cwb);
				deleteCwbDAO.deleteKuFangZaiTu(cwb);
				deleteCwbDAO.deleteKuFangRuKu(cwb);
				deleteCwbDAO.deleteKuDuiKu(cwb);
				deleteCwbDAO.deleteTuiHuoChuZhan(cwb);
				deleteCwbDAO.deleteZongheChaxun(cwb);
			}
		} catch (Exception e) {
			logger.info("数据删除功能：操作人：{}，订单号：{}", cwbAndUserid.split(",")[1], cwb);
		}
	}

	public void deletecwbBatch(@Header("cwbbatchDelete") String cwb) {
		logger.info("--订单失效功能，订单号：{}", cwb);
		try {
			if (cwb.trim().length() > 0) {
				deleteCwbDAO.deleteB2cdataLiantongByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchHappygoByCwb(cwb);
				deleteCwbDAO.deleteB2CDataSearchSaohuobangByCwb(cwb);
				deleteCwbDAO.deleteCwbOrderByCwb(cwb);
				deleteCwbDAO.deleteDeliveryChukuByCwb(cwb);
				deleteCwbDAO.deleteDeliveryDaohuoByOpscwbid(cwb);
				deleteCwbDAO.deleteDeliveryJushouByCwb(cwb);
				deleteCwbDAO.deleteDeliveryStateByCwb(cwb);
				deleteCwbDAO.deleteDeliverySuccessfulByCwb(cwb);
				deleteCwbDAO.deleteDeliveryZhiliuByCwb(cwb);
				deleteCwbDAO.deleteSendB2CCodByCwb(cwb);
				deleteCwbDAO.deleteSendB2CDataByCwb(cwb);
				deleteCwbDAO.deleteTiHuoByCwb(cwb);
				deleteCwbDAO.deleteZhongZhuanByCwb(cwb);
				deleteCwbDAO.deleteTuiHuoZhanRuKu(cwb);
				deleteCwbDAO.deleteKuFangZaiTu(cwb);
				deleteCwbDAO.deleteKuFangRuKu(cwb);
				deleteCwbDAO.deleteKuDuiKu(cwb);
				deleteCwbDAO.deleteTuiHuoChuZhan(cwb);
				deleteCwbDAO.deleteZongheChaxun(cwb);
			}
		} catch (Exception e) {
			logger.info("--订单失效功能：，订单号：{}", cwb);
		}
	}

	public Map<Long, Branch> getBranchMap() {
		if (branchMap == null || branchMap.size() == 0) {
			List<Branch> branchList = getDmpDAO.getAllBranchs();
			putBranchMap(branchList);
			return branchMap;
		} else {
			return branchMap;
		}
	}

	public static Map<Long, Branch> branchMap;
	static { // 静态初始化 ,用于存储站点map
		branchMap = new HashMap<Long, Branch>();
	}

	/**
	 * 添加map值
	 * 
	 * @param branchList
	 */
	public void putBranchMap(List<Branch> branchList) {
		if (branchList != null && branchList.size() > 0) {
			for (Branch branch : branchList) {
				branchMap.put(branch.getBranchid(), branch);
			}
		}

	}

	/**
	 * {"floworderid":5367,"cwb":"25","branchid":186,"credate":
	 * "2012-10-30 17:14:09","userid":999,
	 * "floworderdetail":{"driverid":-1,"nextbranchid":190,"truckid":-1},
	 * "flowordertype":6,"outwarehouseid":14}
	 * 
	 * @param parm
	 */
	public void saveFlow(@Header("orderFlow") String parm) { // 处理jms数据
		long start = System.currentTimeMillis();
		logger.info("进入flow消息，开始：" + start);
		saveAll(parm, 1);
		long end = System.currentTimeMillis();
		try {
			// TODO jms异常写入监控表
			String optime = DateTimeUtil.getNowTime();
			ExpressSysMonitor monitor = expressSysMonitorDAO.getMaxOpt("JMSOMSFlow");
			// 系统上线第一次加载
			if (monitor == null) {
				ExpressSysMonitor newmonitor = new ExpressSysMonitor();
				newmonitor.setOptime(optime);
				newmonitor.setType("JMSOMSFlow");
				expressSysMonitorDAO.save(newmonitor);
			} else {
				// 后续加载 yyyy-MM-dd HH:m
				String preoptime = monitor.getOptime();
				if (!optime.substring(0, 15).equals(preoptime.substring(0, 15))) {
					ExpressSysMonitor newmonitor = new ExpressSysMonitor();
					newmonitor.setOptime(optime);
					newmonitor.setType("JMSOMSFlow");
					expressSysMonitorDAO.save(newmonitor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("进入flow消息，结束：" + end + ",时差：" + (end - start));
	}

	/**
	 * 
	 * @param parm
	 *            参数
	 * @param isSaveFlowAndState
	 *            是否存储flow表 1是存储，0是不存储，用于同步模块数据，不用同步flow表
	 */
	public void saveAll(String parm, int isSaveFlowAndState) {

		logger.debug("orderFlow oms 环节信息处理,{}", parm);
		try {

			DmpOrderFlow orderFlow = dmpOrderFlowReader.readValue(parm);
			// if(orderFlowDAO.getOrderFlowById(orderFlow.getFloworderid())!=null){
			// logger.warn("收到重复信息,id:{}",orderFlow.getFloworderid());
			// return;
			// }
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = cwbOrderWithDeliveryStateReader.readValue(orderFlow.getFloworderdetail());
			DmpCwbOrder dmpcwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			DmpDeliveryState deliveryState = cwbOrderWithDeliveryState.getDeliveryState();
			if (dmpcwbOrder == null) {
				logger.warn("消息不完整，没有订单信息,id:", orderFlow.getFloworderid());
				return;
			}

			saveFlowcreCwbOrder(orderFlow, dmpcwbOrder, deliveryState);
			if (isSaveFlowAndState == 1) {// 存储flow表
				saveFlowcreOrderFlow(orderFlow, dmpcwbOrder);
			}

			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				if (deliveryState == null) {
					logger.warn("反馈的订单没有配送记录,id:{}", orderFlow.getFloworderid());
					return;
				}
				if (isSaveFlowAndState == 1) {// 存储deliveryState表
					this.saveFlowCreDeliveryState(deliveryState);
				}
			}
			// 更新各种时间，以及反馈表字段到主表
			updateAllTimeAndDelivery(orderFlow, dmpcwbOrder, deliveryState);
			// System.out.println("deliveryState:"+deliveryState.getDeliverystate());
			logger.info("RE: orderFlow 处理结束。订单号:{},操作：{}", orderFlow.getCwb(), orderFlow.getFlowordertypeText());
		} catch (Exception e5) {
			e5.printStackTrace();
		}
	}

	/**
	 * 迁移云功能数据的处理方法
	 * 
	 * @param parm
	 * @param isSaveFlowAndState
	 * @param functionid
	 */
	public void saveAllForChange(String parm, int isSaveFlowAndState, long functionid, long issycTime) {
		logger.debug("orderFlow oms 环节信息处理,{}", parm);
		try {

			DmpOrderFlow orderFlow = dmpOrderFlowReader.readValue(parm);
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = cwbOrderWithDeliveryStateReader.readValue(orderFlow.getFloworderdetail());
			DmpCwbOrder dmpcwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			DmpDeliveryState deliveryState = cwbOrderWithDeliveryState.getDeliveryState();
			if (dmpcwbOrder == null) {
				logger.warn("消息不完整，没有订单信息,id:", orderFlow.getFloworderid());
				return;
			}

			saveFlowcreCwbOrder(orderFlow, dmpcwbOrder, deliveryState);
			if (isSaveFlowAndState == 1) {// 存储flow表
				saveFlowcreOrderFlow(orderFlow, dmpcwbOrder);
			}

			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				if (deliveryState == null) {
					logger.warn("反馈的订单没有配送记录,id:{}", orderFlow.getFloworderid());
					return;
				}
				if (isSaveFlowAndState == 1) {// 存储deliveryState表
					this.saveFlowCreDeliveryState(deliveryState);
				}
			}
			// 更新各种时间，以及反馈表字段到主表
			updateAllTimeAndDeliveryForChange(orderFlow, dmpcwbOrder, deliveryState, functionid, issycTime);
			logger.info("RE: orderFlow 处理结束。订单号:{},操作：{}", orderFlow.getCwb(), orderFlow.getFlowordertypeText());
		} catch (Exception e5) {
			e5.printStackTrace();
		}
	}

	// SaveFlow用到的保存订单表
	private void saveFlowcreCwbOrder(DmpOrderFlow dmpOrderFlow, DmpCwbOrder order, DmpDeliveryState deliveryState) {
		logger.info("save orderDetail----------");
		String emaildates = order.getEmaildate() != null && !"".equals(order.getEmaildate()) ? order.getEmaildate() : "";
		CwbOrder cwborder = new CwbOrder();
		cwborder.setCwb(dmpOrderFlow.getCwb());
		cwborder.setSendcarname(order.getSendcarname());
		cwborder.setCaramount(order.getCaramount());
		cwborder.setConsigneename(order.getConsigneename());
		cwborder.setConsigneephone(order.getConsigneephone());
		cwborder.setConsigneeaddress(order.getConsigneeaddress());
		cwborder.setEmaildate(emaildates);
		cwborder.setCustomercommand(order.getCustomercommand());
		cwborder.setDeliverid(order.getDeliverid());
		cwborder.setFlowordertype(dmpOrderFlow.getFlowordertype());
		cwborder.setCustomerid(order.getCustomerid());
		cwborder.setBranchid(order.getDeliverybranchid());
		cwborder.setNextbranchid(order.getNextbranchid());
		cwborder.setStartbranchid(order.getStartbranchid());
		cwborder.setBacktocustomer_awb(order.getBacktocustomer_awb());
		cwborder.setCwbflowflag(order.getCwbflowflag());
		cwborder.setCarrealweight(order.getCarrealweight());
		cwborder.setCartype(order.getCartype());
		cwborder.setCarwarehouse(order.getCarwarehouse());
		cwborder.setCarsize(order.getCarsize());
		cwborder.setSendcarnum(order.getSendcarnum());
		cwborder.setBackcarnum(order.getBackcarnum());
		cwborder.setCaramount(order.getCaramount());
		cwborder.setBackcarname(order.getBackcarname());
		cwborder.setEmailfinishflag(order.getEmailfinishflag());
		cwborder.setReacherrorflag(order.getReacherrorflag());
		cwborder.setOrderflowid(order.getOrderflowid());
		cwborder.setCwbreachbranchid(order.getCwbreachbranchid());
		cwborder.setCwbreachdeliverbranchid(order.getCwbreachdeliverbranchid());
		cwborder.setPodfeetoheadflag(order.getPodfeetoheadflag() == null ? "0" : order.getPodfeetoheadflag());
		cwborder.setBackreason(order.getBackreason());
		cwborder.setBackreasonid(order.getBackreasonid());
		if (order.getPodfeetoheadtime() != null && !"".equals(order.getPodfeetoheadtime())) {
			cwborder.setPodfeetoheadtime(order.getPodfeetoheadtime());
		}
		if (order.getPodfeetoheadchecktime() != null && !"".equals(order.getPodfeetoheadchecktime())) {
			cwborder.setPodfeetoheadchecktime(order.getPodfeetoheadchecktime());
		}
		cwborder.setPodfeetoheadcheckflag(order.getPodfeetoheadcheckflag());
		//滞留二级原因
		if (order.getLeavedreasonid() != 0) {
			cwborder.setLeavedreasonid(order.getLeavedreasonid());
		}
		//滞留一级原因
		if (order.getFirstlevelid() != 0) {
			cwborder.setFirstleavedreasonid(order.getFirstlevelid());
		}
		cwborder.setShouldfare(order.getShouldfare());
		cwborder.setInfactfare(order.getInfactfare());
		if (order.getDeliversubscribeday() != null && !"".equals(order.getDeliversubscribeday())) {
			cwborder.setDeliversubscribeday(order.getDeliversubscribeday());
		}
		cwborder.setCustomerwarehouseid(order.getCustomerwarehouseid());
		cwborder.setServiceareaid(order.getServiceareaid());
		cwborder.setCustomerid(order.getCustomerid());
		// try {
		// Customer customer = getDmpDAO.getCustomer(order.getCustomerid());
		// cwborder.setCustomername(customer.getCustomername());
		// } catch (Exception e) {
		// logger.error("保存供货商名称异常",e);
		// }
		cwborder.setShipcwb(order.getShipcwb());
		cwborder.setConsigneeno(order.getConsigneeno());
		cwborder.setConsigneepostcode(order.getConsigneepostcode());
		cwborder.setCwbremark(order.getCwbremark());
		cwborder.setTransway(order.getTransway());
		cwborder.setCwbprovince(order.getCwbprovince());
		cwborder.setCwbcity(order.getCwbcity());
		cwborder.setCwbcounty(order.getCwbcounty());
		cwborder.setReceivablefee(order.getReceivablefee());
		cwborder.setPaybackfee(order.getPaybackfee());
		cwborder.setShipperid(order.getShipperid());
		cwborder.setCwbordertypeid(order.getCwbordertypeid());
		cwborder.setConsigneemobile(order.getConsigneemobile());
		cwborder.setTranscwb(order.getTranscwb());
		cwborder.setDestination(order.getDestination());
		cwborder.setCwbdelivertypeid(order.getCwbdelivertypeid());
		cwborder.setExceldeliver(order.getExceldeliver());
		cwborder.setExcelbranch(order.getExcelbranch());
		cwborder.setExcelimportuserid(order.getExcelimportuserid());
		cwborder.setState(order.getState());
		cwborder.setEmaildateid(order.getEmaildateid());
		cwborder.setRemark1(order.getRemark1() == null ? "" : order.getRemark1());
		cwborder.setRemark2(order.getRemark2() == null ? "" : order.getRemark2());
		cwborder.setRemark3(order.getRemark3() == null ? "" : order.getRemark3());
		cwborder.setRemark4(order.getRemark4() == null ? "" : order.getRemark4());
		cwborder.setRemark5(order.getRemark5() == null ? "" : order.getRemark5());
		cwborder.setCurrentbranchid(order.getCurrentbranchid());
		cwborder.setDeliverybranchid(order.getDeliverybranchid());
		cwborder.setPayupbranchid(deliveryState == null ? 0 : deliveryState.getDeliverybranchid());
		cwborder.setPaytype_old(order.getPaywayid() + "");
		cwborder.setPaytype(order.getNewpaywayid() + "");
		cwborder.setCustomerbrackhouseremark(order.getCustomerbrackhouseremark());
		cwborder.setResendtime(order.getResendtime());
		cwborder.setAuditstate(deliveryState != null ? deliveryState.getGcaid() : 0);
		cwborder.setBackreasonid(order.getBackreasonid());
		cwborder.setBackreason(order.getBackreason());
		cwborder.setCommonid(order.getCommonid());
		cwborder.setWeishuakareason(order.getWeishuakareason());
		cwborder.setWeishuakareasonid(order.getWeishuakareasonid());
		cwborder.setHistorybranchname(order.getHistorybranchname());
		if (cwbDAO.getCwbByCwbCount(dmpOrderFlow.getCwb()) > 0) {
			cwborder = setOrder(cwborder, deliveryState);
			cwbDAO.updateCwbOrder(cwborder);
		} else {
			cwbDAO.creCwbOrder(cwborder);
		}
	}

	private void saveFlowcreOrderFlow(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		logger.info("RE: save FlowcreOrder");
		OrderFlow of = new OrderFlow();
		of.setFloworderid(orderFlow.getFloworderid());
		of.setCwb(orderFlow.getCwb());
		of.setBranchid(orderFlow.getBranchid());
		of.setCredate(orderFlow.getCredate());
		of.setUserid(orderFlow.getUserid());
		of.setCaramount(dmpCwbOrder.getCaramount());
		// of.setCustomername(customername);
		// of.setUsername(username);//操作员名称
		of.setFlowordertype(orderFlow.getFlowordertype());
		// of.setStartbranchname(stratebranchname);//当前站名称
		// of.setNextbranchname(nextbranchname);//下一站名称
		// of.setCarwarehouse(carwarehousename);//入库仓库
		// of.setDelivername(delivername);//小件员名称
		// of.setTargetcarwarehouseName(targetcarwarehouseName);//目标库房名称
		try {
			of.setEmaildate(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dmpCwbOrder.getEmaildate()).getTime()));
			of.setShiptime(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dmpCwbOrder.getEmaildate()).getTime()));
		} catch (ParseException e) {
		}
		of.setCustomerid((int) dmpCwbOrder.getCustomerid());
		of.setEmaildateid(dmpCwbOrder.getEmaildateid());
		of.setStartbranchid(dmpCwbOrder.getStartbranchid());// 当前站点id
		of.setNextbranchid(dmpCwbOrder.getNextbranchid());// 下一站id
		of.setExcelbranchid(dmpCwbOrder.getDeliverybranchid());// 目的站id
		of.setDeliverid(dmpCwbOrder.getDeliverid());// 小件员id
		orderFlowDAO.creOrderFlow(of);// 存储完当前环节
	}

	private void saveFlowCreDeliveryState(DmpDeliveryState deliverystate) {
		String cwb = deliverystate.getCwb();
		long deliveryid = deliverystate.getDeliveryid();
		BigDecimal receivedfee = deliverystate.getReceivedfee();
		BigDecimal returnedfee = deliverystate.getReturnedfee();
		BigDecimal businessfee = deliverystate.getBusinessfee();
		String cwbordertypeid = deliverystate.getCwbordertypeid();
		long deliverystate1 = deliverystate.getDeliverystate();
		BigDecimal cash = deliverystate.getCash();
		BigDecimal pos = deliverystate.getPos();
		BigDecimal checkfee = deliverystate.getCheckfee();
		long gobackid = deliverystate.getGcaid();
		String goodsname = "";
		String sendcarname = deliverystate.getSendcarname();
		BigDecimal caramount = BigDecimal.ZERO;
		String consigneename = deliverystate.getConsigneename();
		String consigneephone = deliverystate.getConsigneephone();
		String consigneeaddress = deliverystate.getConsigneeaddress();
		String emaildate = deliverystate.getEmaildate();
		BigDecimal otherfee = deliverystate.getOtherfee();
		long branchid = 0;
		String deliveryName = deliverystate.getDeliverealname();
		String posremark = deliverystate.getPosremark();
		String createtime = deliverystate.getCreatetime();
		String shangmenlanshoutime = deliverystate.getShangmenlanshoutime();
		deliveryStateDAO.creDeliveryState(cwb, deliveryid, receivedfee, returnedfee, businessfee, cwbordertypeid, deliverystate1, cash, pos, checkfee, gobackid, goodsname, sendcarname, caramount,
				consigneename, consigneephone, consigneeaddress, emaildate, otherfee, branchid, deliveryName, posremark, createtime, shangmenlanshoutime);

	}

	/**
	 * 妥投订单存储
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	public void saveDeliverySuccessful(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {
		if (deliverystate == null) {
			return;
		}
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()
				|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) {
			return;
		}
		String cwb = deliverystate.getCwb();
		List<DeliverySuccessful> dList = deliverySuccessfulDAO.checkDeliverySuccessful(cwb);// 是否存在其他状态的记录
		if (dList != null && dList.size() > 0) {// 已存在，进行更新
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				deliverySuccessfulDAO.deleteDeliverySuccessful(cwb);
				return;
			}
			if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue())
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dList.get(0).getDeliverytime())) {
				return;
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dList.get(0).getAudittime())) {
				return;
			}
			if (deliverystate.getDeliverstateremark().indexOf("chinaums") > -1 || deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()
					|| deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				long deliveryid = deliverystate.getDeliveryid();
				// 按订单类型
				long deliverystate1 = deliverystate.getDeliverystate();
				if (deliverystate1 == 0) {
					if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
						deliverystate1 = CwbOrderTypeIdEnum.Peisong.getValue();
					} else if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
						deliverystate1 = CwbOrderTypeIdEnum.Shangmentui.getValue();
					} else if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
						deliverystate1 = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
					}
				}
				long branchid = orderFlow.getBranchid();
				DeliverySuccessful del = dList.get(0);
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					del.setDeliverytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				}
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					del.setAuditstate(deliverystate.getGcaid());// gcaid
					del.setAudittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				}
				del.setBranchid(branchid);
				del.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				del.setCustomerid(dmpCwbOrder.getCustomerid());
				del.setCwb(cwb);
				del.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
				del.setDeliveryid(deliveryid);
				del.setDeliverystate(deliverystate1);
				del.setDeliverystateid(deliverystate.getId());
				del.setPaywayid(Long.parseLong(dmpCwbOrder.getNewpaywayid()));
				deliverySuccessfulDAO.saveDeliverySuccessful(del);
				saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.TuoTOuDingDanHuiZong.getText(),
						UpdateMessageMenuNameEnum.TuoTOuDingDanHuiZong.getValue());
				return;
			} else {// 不属于妥投，说明是反馈修改了结果，把记录删除掉
				deliverySuccessfulDAO.deleteDeliverySuccessful(cwb);
				return;
			}
		} else if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.CheXiaoFanKui.getValue()
				&& (deliverystate.getDeliverstateremark().indexOf("chinaums") > -1 || deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()
						|| deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {// 不存在，进行插入
			long deliveryid = deliverystate.getDeliveryid();
			// 按订单类型
			long deliverystate1 = deliverystate.getDeliverystate();
			if (deliverystate1 == 0) {
				if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
					deliverystate1 = CwbOrderTypeIdEnum.Peisong.getValue();
				} else if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
					deliverystate1 = CwbOrderTypeIdEnum.Shangmentui.getValue();
				} else if (dmpCwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
					deliverystate1 = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
				}
			}
			long branchid = orderFlow.getBranchid();
			DeliverySuccessful del = new DeliverySuccessful();
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				del.setDeliverytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				del.setAuditstate(deliverystate.getGcaid());
				del.setAudittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			}
			del.setBranchid(branchid);
			del.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			del.setCustomerid(dmpCwbOrder.getCustomerid());
			del.setCwb(cwb);
			del.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			del.setDeliveryid(deliveryid);
			del.setDeliverystate(deliverystate1);
			del.setDeliverystateid(deliverystate.getId());
			del.setPaywayid(Long.parseLong(dmpCwbOrder.getNewpaywayid()));
			del.setReceivablefee(dmpCwbOrder.getReceivablefee());
			del.setPaybackfee(dmpCwbOrder.getPaybackfee());
			deliverySuccessfulDAO.creDeliverySuccessful(del);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.TuoTOuDingDanHuiZong.getText(),
					UpdateMessageMenuNameEnum.TuoTOuDingDanHuiZong.getValue());
			return;
		} else {// 不属于妥投订单
			return;
		}

	}

	public void saveDeliveryChuKu(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			return;
		}

		Map<Long, Branch> branchNewMap = getBranchMap();
		Branch startBranch = branchNewMap.get(dmpCwbOrder.getStartbranchid());
		Branch nextBranch = branchNewMap.get(dmpCwbOrder.getNextbranchid());
		if (startBranch.getSitetype() != BranchEnum.KuFang.getValue() || nextBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {// 是中转站的
																																		// return
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<DeliveryChuku> dList = deliveryChukuDAO.checkDeliveryChuku(cwb);// 是否存在记录
		if (dList != null && dList.size() > 0) {// 已存在，进行更新
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dList.get(0).getOutstoreroomtime())) {
				return;
			}
			DeliveryChuku dechuku = new DeliveryChuku();

			dechuku.setOutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			dechuku.setEmaildate(dmpCwbOrder.getEmaildate());
			dechuku.setStartbranchid(dmpCwbOrder.getStartbranchid());
			dechuku.setCustomerid(dmpCwbOrder.getCustomerid());
			dechuku.setCwb(cwb);
			dechuku.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			dechuku.setNextbranchid(dmpCwbOrder.getNextbranchid());
			dechuku.setReceivablefee(dmpCwbOrder.getReceivablefee());
			dechuku.setPaybackfee(dmpCwbOrder.getPaybackfee());
			dechuku.setOutstoreroomtimeuserid(orderFlow.getUserid());
			deliveryChukuDAO.saveDeliveryChuku(dechuku);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getValue());
			return;
		} else {// 不存在，进行插入
			DeliveryChuku dechuku = new DeliveryChuku();

			dechuku.setOutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			dechuku.setEmaildate(dmpCwbOrder.getEmaildate());
			dechuku.setStartbranchid(dmpCwbOrder.getStartbranchid());
			dechuku.setCustomerid(dmpCwbOrder.getCustomerid());
			dechuku.setCwb(cwb);
			dechuku.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			dechuku.setNextbranchid(dmpCwbOrder.getNextbranchid());
			dechuku.setReceivablefee(dmpCwbOrder.getReceivablefee());
			dechuku.setPaybackfee(dmpCwbOrder.getPaybackfee());
			dechuku.setOutstoreroomtimeuserid(orderFlow.getUserid());
			deliveryChukuDAO.creDeliveryChuku(dechuku);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getValue());
			return;
		}
	}

	public void saveKDKDeliveryChuKu(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<KDKDeliveryChuku> dList = kdkDeliveryChukuDAO.checkKDKDeliveryChuku(cwb);// 是否存在记录
		if (dList != null && dList.size() > 0) {// 已存在，进行更新
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dList.get(0).getOutstoreroomtime())) {
				return;
			}
			KDKDeliveryChuku kdkDeliveryChuku = new KDKDeliveryChuku();

			kdkDeliveryChuku.setOutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			kdkDeliveryChuku.setEmaildate(dmpCwbOrder.getEmaildate());
			kdkDeliveryChuku.setStartbranchid(dmpCwbOrder.getStartbranchid());
			kdkDeliveryChuku.setCustomerid(dmpCwbOrder.getCustomerid());
			kdkDeliveryChuku.setCwb(cwb);
			kdkDeliveryChuku.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			kdkDeliveryChuku.setNextbranchid(dmpCwbOrder.getNextbranchid());
			kdkDeliveryChuku.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kdkDeliveryChuku.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kdkDeliveryChukuDAO.saveKDKDeliveryChuku(kdkDeliveryChuku);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.KDKKuFangChuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.KDKKuFangChuKuTongJi.getValue());
			return;
		} else {// 不存在，进行插入
			KDKDeliveryChuku kdkDeliveryChuku = new KDKDeliveryChuku();

			kdkDeliveryChuku.setOutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			kdkDeliveryChuku.setEmaildate(dmpCwbOrder.getEmaildate());
			kdkDeliveryChuku.setStartbranchid(dmpCwbOrder.getStartbranchid());
			kdkDeliveryChuku.setCustomerid(dmpCwbOrder.getCustomerid());
			kdkDeliveryChuku.setCwb(cwb);
			kdkDeliveryChuku.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			kdkDeliveryChuku.setNextbranchid(dmpCwbOrder.getNextbranchid());
			kdkDeliveryChuku.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kdkDeliveryChuku.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kdkDeliveryChukuDAO.creKDKDeliveryChuku(kdkDeliveryChuku);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.KDKKuFangChuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.KDKKuFangChuKuTongJi.getValue());
			return;
		}
	}

	public void saveDeliveryDaohuo(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<DeliveryDaohuo> dList = deliveryDaohuoDAO.checkDeliveryDaohuo(cwb);// 是否存在记录
		Map<Long, Branch> branchNewMap = getBranchMap();
		Branch b = branchNewMap.get(dmpCwbOrder.getStartbranchid());
		if (b != null && b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			zhongZhuanDAO.saveZhongZhuanInsiteByCwb(orderFlow.getBranchid(), new SimpleDateFormat("yyyy-HH-dd hh:MM:ss").format(orderFlow.getCredate()), dmpCwbOrder.getCwb());
		}
		if (dList != null && dList.size() > 0) {// 已存在，进行更新
			if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dList.get(0).getInSitetime())) {
				return;
			}
			DeliveryDaohuo dedaohuo = new DeliveryDaohuo();
			dedaohuo.setCurrentbranchid(orderFlow.getBranchid());
			dedaohuo.setInSitetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			dedaohuo.setEmaildate(dmpCwbOrder.getEmaildate());
			dedaohuo.setStartbranchid(dmpCwbOrder.getStartbranchid());
			dedaohuo.setCustomerid(dmpCwbOrder.getCustomerid());
			dedaohuo.setCwb(cwb);
			dedaohuo.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			dedaohuo.setNextbranchid(dmpCwbOrder.getNextbranchid());
			dedaohuo.setReceivablefee(dmpCwbOrder.getReceivablefee());
			dedaohuo.setPaybackfee(dmpCwbOrder.getPaybackfee());
			dedaohuo.setIsnow(1);
			deliveryDaohuoDAO.saveDeliveryDaohuoByCwb(dedaohuo);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getText(),
					UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getValue());
			return;
		} else {// 不存在，进行插入
			DeliveryDaohuo dedaohuo = new DeliveryDaohuo();
			dedaohuo.setCurrentbranchid(orderFlow.getBranchid());
			dedaohuo.setInSitetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			dedaohuo.setEmaildate(dmpCwbOrder.getEmaildate());
			dedaohuo.setStartbranchid(dmpCwbOrder.getStartbranchid());
			dedaohuo.setCustomerid(dmpCwbOrder.getCustomerid());
			dedaohuo.setCwb(cwb);
			dedaohuo.setCwbordertypeid(dmpCwbOrder.getCwbordertypeid());
			dedaohuo.setNextbranchid(dmpCwbOrder.getNextbranchid());
			dedaohuo.setReceivablefee(dmpCwbOrder.getReceivablefee());
			dedaohuo.setPaybackfee(dmpCwbOrder.getPaybackfee());
			deliveryDaohuoDAO.creDeliveryDaohuo(dedaohuo);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getText(),
					UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getValue());
			return;
		}
	}

	/**
	 * 退货出站 统计 迁移数据
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 */
	private void saveTuiHuoChuZhan(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<TuiHuoChuZhanOrder> tlist = tuiHuoChuZhanDao.checkIsExist(cwb);
		if (tlist != null && tlist.size() > 0) {// 判断 该订单是否已存在 若存在 更新
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(tlist.get(0).getTuihuochuzhantime())) {
				return;
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(tlist.get(0).getTuihuozhanrukutime())) {
				return;
			}

			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				TuiHuoChuZhanOrder to = new TuiHuoChuZhanOrder();
				to.setTuihuochuzhantime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				to.setTuihuozhanrukutime("");
				to.setCwb(dmpCwbOrder.getCwb());
				to.setCustomerid(dmpCwbOrder.getCustomerid());
				to.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				to.setTuihuobranchid(dmpCwbOrder.getDeliverybranchid());
				tuiHuoChuZhanDao.updateTuiHuoChuZhan(to);
			} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				TuiHuoChuZhanOrder to = tlist.get(0);
				to.setTuihuozhanrukutime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				to.setCustomerid(dmpCwbOrder.getCustomerid());
				to.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				to.setTuihuobranchid(dmpCwbOrder.getDeliverybranchid());
				tuiHuoChuZhanDao.updateTuiHuoChuZhan(to);
			}

		} else {
			TuiHuoChuZhanOrder to = new TuiHuoChuZhanOrder();
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				to.setTuihuochuzhantime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				to.setTuihuozhanrukutime("");
			}// 如果 是退货入库 怎么处理
			to.setCustomerid(dmpCwbOrder.getCustomerid());
			to.setCwb(dmpCwbOrder.getCwb());
			to.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			to.setTuihuobranchid(dmpCwbOrder.getDeliverybranchid());
			tuiHuoChuZhanDao.saveTuiHuoChuZhan(to);
		}
	}

	/**
	 * 退货站入库统计（云）
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	private void saveTuiHuoZhanRuKu(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {
		if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<TuiHuoZhanRuKuOrder> tlist = tuiHuoZhanRuKuDao.checkIsExist(cwb);
		if (tlist != null && tlist.size() > 0) {// 判断 该订单是否已存在 若存在 更新
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()
					&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(tlist.get(0).getRukutime())) {
				return;
			}
			TuiHuoZhanRuKuOrder tuiHuoZhanRuKuOrder = tlist.get(0);
			tuiHuoZhanRuKuOrder.setCustomerid(dmpCwbOrder.getCustomerid());
			tuiHuoZhanRuKuOrder.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			tuiHuoZhanRuKuOrder.setDeliverystateid(deliverystate == null ? 0 : deliverystate.getId());
			tuiHuoZhanRuKuOrder.setPaybackfee(dmpCwbOrder.getPaybackfee());
			tuiHuoZhanRuKuOrder.setReceivablefee(dmpCwbOrder.getReceivablefee());
			tuiHuoZhanRuKuOrder.setRukutime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			tuiHuoZhanRuKuOrder.setTuihuobranchid(dmpCwbOrder.getDeliverybranchid());
			tuiHuoZhanRuKuDao.updateTuiHuoZhanRuKu(tuiHuoZhanRuKuOrder);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.TuiHuoZhanRuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.TuiHuoZhanRuKuTongJi.getValue());
		} else {
			TuiHuoZhanRuKuOrder tuiHuoZhanRuKuOrder = new TuiHuoZhanRuKuOrder();
			tuiHuoZhanRuKuOrder.setCustomerid(dmpCwbOrder.getCustomerid());
			tuiHuoZhanRuKuOrder.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			tuiHuoZhanRuKuOrder.setDeliverystateid(deliverystate == null ? 0 : deliverystate.getId());
			tuiHuoZhanRuKuOrder.setPaybackfee(dmpCwbOrder.getPaybackfee());
			tuiHuoZhanRuKuOrder.setReceivablefee(dmpCwbOrder.getReceivablefee());
			tuiHuoZhanRuKuOrder.setRukutime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			tuiHuoZhanRuKuOrder.setTuihuobranchid(dmpCwbOrder.getDeliverybranchid());
			tuiHuoZhanRuKuOrder.setCwb(dmpCwbOrder.getCwb());
			tuiHuoZhanRuKuDao.creTuiHuoZhanRuKu(tuiHuoZhanRuKuOrder);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.TuiHuoZhanRuKuTongJi.getText(),
					UpdateMessageMenuNameEnum.TuiHuoZhanRuKuTongJi.getValue());
		}
	}

	public void saveDeliveryDaohuoIsNow(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
			return;
		}
		// 根据订单号和操作时间判断记录是否存在，到站时间早于该次操作时间的才符合条件
		List<DeliveryDaohuo> dList = deliveryDaohuoDAO.checkDeliveryDaohuoByCwbAndInSitetime(dmpCwbOrder.getCwb(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
		if (dList != null && dList.size() > 0) {
			deliveryDaohuoDAO.updateDeliveryDaohuoIsnow(dmpCwbOrder.getCwb());
		}
	}

	public void saveZhongZhuan(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && orderFlow.getFlowordertype() != FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {
			return;
		}
		Map<Long, Branch> branchNewMap = getBranchMap();
		Branch startBranch = branchNewMap.get(dmpCwbOrder.getStartbranchid());
		Branch nextBranch = branchNewMap.get(dmpCwbOrder.getNextbranchid());

		if (!((orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) && startBranch != null && nextBranch != null && (startBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue() || nextBranch
				.getSitetype() == BranchEnum.ZhongZhuan.getValue()))) {// 中转站
			// 发出站或者目的站不是中转类型的
			return;
		}
		// 该单到货信息
		List<DeliveryDaohuo> daohuoList = deliveryDaohuoDAO.checkDeliveryDaohuo(orderFlow.getCwb());
		DeliveryDaohuo daohuo = daohuoList.size() > 0 ? daohuoList.get(0) : new DeliveryDaohuo();

		// 根据cwb，startbranchid，nextbranchid判断记录是否存在
		List<ZhongZhuan> zhongZhuanList = new ArrayList<ZhongZhuan>();

		if (startBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			zhongZhuanList = zhongZhuanDAO.checkZhongZhuanAndStartbranchid(dmpCwbOrder.getCwb(), dmpCwbOrder.getStartbranchid());
		} else if (nextBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			zhongZhuanList = zhongZhuanDAO.checkZhongZhuanAndNextbranchid(dmpCwbOrder.getCwb(), dmpCwbOrder.getNextbranchid());
		}
		if (zhongZhuanList != null && zhongZhuanList.size() > 0) {// 已存在，进行更新
			ZhongZhuan cwbzhongZhuan = zhongZhuanList.get(0);
			if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(cwbzhongZhuan.getZhongzhuanoutstoreroomtime())) {
				return;
			}
			ZhongZhuan zhongZhuan = new ZhongZhuan();
			zhongZhuan.setId(cwbzhongZhuan.getId());
			zhongZhuan.setZhongzhuanoutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			zhongZhuan.setStartbranchid(dmpCwbOrder.getStartbranchid());
			zhongZhuan.setCustomerid(dmpCwbOrder.getCustomerid());
			zhongZhuan.setCwb(dmpCwbOrder.getCwb());
			zhongZhuan.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			zhongZhuan.setNextbranchid(dmpCwbOrder.getNextbranchid());
			zhongZhuan.setReceivablefee(dmpCwbOrder.getReceivablefee());
			zhongZhuan.setPaybackfee(dmpCwbOrder.getPaybackfee());
			zhongZhuan.setInsitebranchid(daohuo.getCurrentbranchid());
			zhongZhuan.setInSitetime(daohuo.getInSitetime());
			zhongZhuanDAO.saveZhongZhuanByCwb(zhongZhuan);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZhongZhuanDingDanTongJi.getText(),
					UpdateMessageMenuNameEnum.ZhongZhuanDingDanTongJi.getValue());
			return;
		} else {// 不存在，进行插入
			ZhongZhuan zhongZhuan = new ZhongZhuan();
			zhongZhuan.setZhongzhuanoutstoreroomtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			zhongZhuan.setStartbranchid(dmpCwbOrder.getStartbranchid());
			zhongZhuan.setCustomerid(dmpCwbOrder.getCustomerid());
			zhongZhuan.setCwb(dmpCwbOrder.getCwb());
			zhongZhuan.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			zhongZhuan.setNextbranchid(dmpCwbOrder.getNextbranchid());
			zhongZhuan.setReceivablefee(dmpCwbOrder.getReceivablefee());
			zhongZhuan.setPaybackfee(dmpCwbOrder.getPaybackfee());
			zhongZhuan.setInsitebranchid(daohuo.getCurrentbranchid());
			zhongZhuan.setInSitetime(daohuo.getInSitetime());
			zhongZhuanDAO.creZhongZhuan(zhongZhuan);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZhongZhuanDingDanTongJi.getText(),
					UpdateMessageMenuNameEnum.ZhongZhuanDingDanTongJi.getValue());
			return;
		}
	}

	/**
	 * 处理 分站滞留 迁移数据
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 */

	private void saveDeliveryZhiLiu(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {

		if (deliverystate == null) {
			return;
		}
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) {
			return;
		}

		String cwb = dmpCwbOrder.getCwb();
		List<DeliveryZhiLiu> dlist = deliveryZhiLiuDAO.checkIsExist(cwb);

		if (dlist != null && dlist.size() > 0 && deliverystate.getDeliverystate() != DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			// 如果订单存在，但是现在的配送结果不是分站滞留，那么删除数据（说明该单配送结果已经修改）
			deliveryZhiLiuDAO.deleteDeliveryZhiLiu(cwb);
			return;
		} else {
			if (deliverystate.getDeliverystate() != DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				return;
			}
			if (dlist != null && dlist.size() > 0) {// 检查该订单是否已存在 若存在 更新
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()
						&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dlist.get(0).getDeliverytime())) {
					return;
				}
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()
						&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dlist.get(0).getAudittime())) {
					return;
				}
				DeliveryZhiLiu dzl = new DeliveryZhiLiu();
				dzl.setAudittime(deliverystate.getAuditingtime());
				dzl.setBranchid(orderFlow.getBranchid());
				dzl.setCustomerid(dmpCwbOrder.getCustomerid());
				dzl.setCwb(dmpCwbOrder.getCwb());
				dzl.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				dzl.setDeliveryid(deliverystate.getDeliveryid());
				dzl.setDeliverystateid(deliverystate.getId());
				dzl.setDeliverytime(deliverystate.getDeliverytime());
				dzl.setEmaildate(dmpCwbOrder.getEmaildate());
				dzl.setGcaid(deliverystate.getGcaid());
				dzl.setPaybackfee(dmpCwbOrder.getPaybackfee());
				dzl.setReceivablefee(dmpCwbOrder.getReceivablefee());
				dzl.setResendtime(dmpCwbOrder.getResendtime());
				deliveryZhiLiuDAO.updateZhiLiu(dzl);
				saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZhiLiuDingDanHuiZong.getText(),
						UpdateMessageMenuNameEnum.ZhiLiuDingDanHuiZong.getValue());

			} else {// 不存在 插入
				DeliveryZhiLiu dzl = new DeliveryZhiLiu();
				dzl.setAudittime(deliverystate.getAuditingtime());
				dzl.setBranchid(orderFlow.getBranchid());
				dzl.setCustomerid(dmpCwbOrder.getCustomerid());
				dzl.setCwb(dmpCwbOrder.getCwb());
				dzl.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				dzl.setDeliveryid(deliverystate.getDeliveryid());
				dzl.setDeliverystateid(deliverystate.getId());
				dzl.setDeliverytime(deliverystate.getDeliverytime());
				dzl.setEmaildate(dmpCwbOrder.getEmaildate());
				dzl.setGcaid(deliverystate.getGcaid());
				dzl.setPaybackfee(dmpCwbOrder.getPaybackfee());
				dzl.setReceivablefee(dmpCwbOrder.getReceivablefee());
				dzl.setResendtime(dmpCwbOrder.getResendtime());
				deliveryZhiLiuDAO.creDeliveryZhiLiu(dzl);
				saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZhiLiuDingDanHuiZong.getText(),
						UpdateMessageMenuNameEnum.ZhiLiuDingDanHuiZong.getValue());
			}
		}
	}

	/**
	 * 处理 拒收订单汇总 数据
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	private void saveDeliverJuShou(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {
		if (deliverystate == null) {
			return;
		}
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<DeliveryJuShou> dlist = deliveryJuShouDAO.checkIsExist(cwb);
		if (dlist != null
				&& dlist.size() > 0
				&& !(deliverystate.getDeliverystate() == (DeliveryStateEnum.QuanBuTuiHuo.getValue()) || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()
						|| deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) {
			// 如果订单存在，但是现在的配送结果不是分站滞留，那么删除数据（说明该单配送结果已经修改）
			deliveryJuShouDAO.delJuShouByCwb(cwb);
			return;
		} else {
			if (!(deliverystate.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()
					|| deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				return;
			}
			if (dlist != null && dlist.size() > 0) {// 检查该订单是否已存在 若存在 更新
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()
						&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dlist.get(0).getDeliverytime())) {
					return;
				}
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()
						&& new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(dlist.get(0).getAudittime())) {
					return;
				}

				DeliveryJuShou dzl = new DeliveryJuShou();
				dzl.setDeliverystate(deliverystate.getDeliverystate());
				dzl.setAudittime(deliverystate.getAuditingtime());
				dzl.setBranchid(orderFlow.getBranchid());
				dzl.setCustomerid(dmpCwbOrder.getCustomerid());
				dzl.setCwb(dmpCwbOrder.getCwb());
				dzl.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				dzl.setDeliveryid(dmpCwbOrder.getDeliverid());
				dzl.setDeliverystateid(deliverystate.getId());
				dzl.setDeliverytime(deliverystate.getDeliverytime());
				dzl.setEmaildate(dmpCwbOrder.getEmaildate());
				dzl.setGcaid(deliverystate.getGcaid());
				dzl.setPaybackfee(dmpCwbOrder.getPaybackfee());
				dzl.setReceivablefee(dmpCwbOrder.getReceivablefee());
				deliveryJuShouDAO.update(dzl);
			} else {// 不存在 插入
				DeliveryJuShou dzl = new DeliveryJuShou();
				dzl.setDeliverystate(deliverystate.getDeliverystate());
				dzl.setAudittime(deliverystate.getAuditingtime());
				dzl.setBranchid(orderFlow.getBranchid());
				dzl.setCustomerid(dmpCwbOrder.getCustomerid());
				dzl.setCwb(dmpCwbOrder.getCwb());
				dzl.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
				dzl.setDeliveryid(dmpCwbOrder.getDeliverid());
				dzl.setDeliverystateid(deliverystate.getId());
				dzl.setDeliverytime(deliverystate.getDeliverytime());
				dzl.setEmaildate(dmpCwbOrder.getEmaildate());
				dzl.setGcaid(deliverystate.getGcaid());
				dzl.setPaybackfee(dmpCwbOrder.getPaybackfee());
				dzl.setReceivablefee(dmpCwbOrder.getReceivablefee());
				deliveryJuShouDAO.save(dzl);
			}
		}

	}

	private void saveCwbTiHuo(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<CwbTiHuo> tihuolist = cwbTiHuoDAO.checkCwbTiHuo(cwb);
		if (tihuolist != null && tihuolist.size() > 0) {// 检查该订单是否已存在 若存在 更新
			CwbTiHuo co = tihuolist.get(0);
			if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(co.getTihuotime())) {
				return;
			}

			CwbTiHuo cwbTiHuo = new CwbTiHuo();
			cwbTiHuo.setCustomerid(dmpCwbOrder.getCustomerid());
			cwbTiHuo.setCwb(dmpCwbOrder.getCwb());
			cwbTiHuo.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			cwbTiHuo.setCurrentbranchid(dmpCwbOrder.getCurrentbranchid());
			cwbTiHuo.setTihuotime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			cwbTiHuo.setUserid(orderFlow.getUserid());
			cwbTiHuo.setEmaildate(dmpCwbOrder.getEmaildate());
			cwbTiHuo.setPaybackfee(dmpCwbOrder.getPaybackfee());
			cwbTiHuo.setReceivablefee(dmpCwbOrder.getReceivablefee());
			cwbTiHuo.setId(co.getId());
			cwbTiHuoDAO.saveCwbTiHuoById(cwbTiHuo);
		} else {// 不存在 插入
			CwbTiHuo cwbTiHuo = new CwbTiHuo();
			cwbTiHuo.setCustomerid(dmpCwbOrder.getCustomerid());
			cwbTiHuo.setCwb(dmpCwbOrder.getCwb());
			cwbTiHuo.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			cwbTiHuo.setCurrentbranchid(dmpCwbOrder.getCurrentbranchid());
			cwbTiHuo.setTihuotime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			cwbTiHuo.setUserid(orderFlow.getUserid());
			cwbTiHuo.setEmaildate(dmpCwbOrder.getEmaildate());
			cwbTiHuo.setPaybackfee(dmpCwbOrder.getPaybackfee());
			cwbTiHuo.setReceivablefee(dmpCwbOrder.getReceivablefee());
			cwbTiHuoDAO.creCwbTiHuo(cwbTiHuo);
		}
		saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.TiHuoDingDanTongJi.getText(),
				UpdateMessageMenuNameEnum.TiHuoDingDanTongJi.getValue());
	}

	/**
	 * 处理更新各种操作时间和反馈的信息
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	public void updateAllTimeAndDelivery(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()
				|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {// 处理妥投迁移
			try {
				this.saveDeliverySuccessful(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("妥投迁移异常", e);
			}

		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {// 处理库房出库迁移
			try {
				this.saveDeliveryChuKu(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("库房出库迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {// 处理库对库出库迁移
			try {
				this.saveKDKDeliveryChuKu(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("库对库出库迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {// 处理分站到货迁移
			try {
				this.saveDeliveryDaohuo(orderFlow, dmpCwbOrder);
				// 删除 库房在途中的订单
				kuFangZaiTuDao.deleteByCwb(orderFlow.getCwb());
			} catch (Exception e) {
				logger.error("分站到货迁移异常", e);
			}
		} else {
			try {
				this.saveDeliveryDaohuoIsNow(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("非到货更改到货表中的状态迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {// 退货出站统计
			try {
				this.saveTuiHuoChuZhan(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("退货出站统计迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {// 分站滞留
																																								// 数据迁移
			try {
				this.saveDeliveryZhiLiu(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("分站滞留迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {// 处理中转订单统计迁移
			try {
				this.saveZhongZhuan(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("中转订单统计迁移异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {// 拒收订单汇总
																																								// 数据迁移
			try {
				this.saveDeliverJuShou(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("拒收订单汇总异常", e);
			}
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) {// 提货订单统计
																																									// 数据迁移
			try {
				this.saveCwbTiHuo(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("提货订单统计异常", e);
			}
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			try {
				this.saveTuiHuoZhanRuKu(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("退货站入库统计(云)，数据迁移错误，flowfromjmsservice", e);
			}
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			try {
				this.saveKuFangZaiTuTongJi(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("库房在途统计(云)，数据迁移错误，flowfromjmsservice", e);
			}
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()
				|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue()) {
			try {
				this.saveKuFangRuKuJi(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("库房入库统计(云)，数据迁移错误，flowfromjmsservice", e);
			}
		}
		// Alan add start
		/**
		 * 保存每次操作
		 */

		try {
			Map<Long, Branch> branchNewMap = getBranchMap();
			Branch startbranch = branchNewMap.get(orderFlow.getBranchid());
			Branch nextbranch = branchNewMap.get(dmpCwbOrder.getNextbranchid()) == null ? new Branch() : branchNewMap.get(dmpCwbOrder.getNextbranchid());
			cwbOrderTailService.saveCwbOrderCurHandle(orderFlow, dmpCwbOrder, deliverystate, startbranch, nextbranch);
			saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZongHeChaXun.getText(),
					UpdateMessageMenuNameEnum.ZongHeChaXun.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("综合查询（云），数据迁移错误，flowfromjmsservice", e);
		}
		// Alan add end

		String cwb = orderFlow.getCwb();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate());
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
				|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {// 中转站入库、出库
																																																// 时间处理
			Map<Long, Branch> branchNewMap = getBranchMap();
			Branch b = branchNewMap.get(orderFlow.getBranchid());
			if (b != null && b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {// 中转站
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
					cwbDAO.updateFlowTime(cwb, time, "zhongzhuanrukutime");
				} else {
					cwbDAO.updateFlowTime(cwb, time, "zhongzhuanzhanchukutime");
				}
				return;
			}
		}

		String flowTimeName = getFlowTimeName(orderFlow.getFlowordertype());
		if (!flowTimeName.equals("")) {// 处理更新操作时间
			cwbDAO.updateFlowTime(cwb, time, flowTimeName);
		} else {
			cwbDAO.updateFlowTime(cwb, time);
		}

	}

	/**
	 * 库房入库统计
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 */
	private void saveKuFangRuKuJi(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder) {
		if (!(orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo
				.getValue())) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<KuFangRuKuOrder> kuFangRuKuOrders = kuFangRuKuDao.checkCwbIsExist(cwb);
		if (kuFangRuKuOrders != null && kuFangRuKuOrders.size() > 0) {// 检查该订单是否已存在
																		// 若存在
																		// 更新
			KuFangRuKuOrder kf = kuFangRuKuOrders.get(0);
			if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(kf.getIntowarehousetime())) {
				return;
			}
			if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(kf.getEmaildate())) {
				return;
			}
			kf.setCustomerid(dmpCwbOrder.getCustomerid());
			kf.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			kf.setDeliverybranchid(dmpCwbOrder.getDeliverybranchid());
			kf.setIntobranchid(orderFlow.getBranchid());// currentbranchid ?
			kf.setEmaildate(dmpCwbOrder.getEmaildate());
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				kf.setIntowarehousetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				kf.setIsruku(1);
			} else {
				kf.setEmaildate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				kf.setIsruku(0);
			}
			kf.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kf.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kf.setIntowarehouseuserid(orderFlow.getUserid());
			kuFangRuKuDao.saveKuFangRuKu(kf);
		} else {// 不存在 插入
			KuFangRuKuOrder kf = new KuFangRuKuOrder();
			kf.setCwb(dmpCwbOrder.getCwb());
			kf.setCustomerid(dmpCwbOrder.getCustomerid());
			kf.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			kf.setDeliverybranchid(dmpCwbOrder.getDeliverybranchid());
			kf.setIntobranchid(orderFlow.getBranchid());
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				kf.setIsruku(1);
				kf.setIntowarehousetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			} else {
				kf.setIsruku(0);
				kf.setEmaildate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			}
			kf.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kf.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kf.setIntowarehouseuserid(orderFlow.getUserid());
			kuFangRuKuDao.creKuFangRuKu(kf);
		}
	}

	/**
	 * 库房在途统计 数据迁移
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	private void saveKuFangZaiTuTongJi(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate) {
		if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			return;
		}
		String cwb = dmpCwbOrder.getCwb();
		List<KuFangZaiTuOrder> kuFangZaiTuOrders = kuFangZaiTuDao.checkCwbIsExist(cwb);
		if (kuFangZaiTuOrders != null && kuFangZaiTuOrders.size() > 0) {// 检查该订单是否已存在
																		// 若存在
																		// 更新
			KuFangZaiTuOrder kf = kuFangZaiTuOrders.get(0);
			if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()).equals(kf.getOutwarehousetime())) {
				return;
			}
			kf.setCustomerid(dmpCwbOrder.getCustomerid());
			kf.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			kf.setDeliverybranchid(dmpCwbOrder.getDeliverybranchid());
			kf.setEmaildate(dmpCwbOrder.getEmaildate());
			kf.setNextbranchid(dmpCwbOrder.getNextbranchid());
			kf.setOutbranchid(dmpCwbOrder.getStartbranchid());
			kf.setOutwarehousetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			kf.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kf.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kuFangZaiTuDao.saveKuFangZaiTu(kf);
		} else {// 不存在 插入
			KuFangZaiTuOrder kf = new KuFangZaiTuOrder();
			kf.setCustomerid(dmpCwbOrder.getCustomerid());
			kf.setCwbordertypeid(Long.parseLong(dmpCwbOrder.getCwbordertypeid()));
			kf.setDeliverybranchid(dmpCwbOrder.getDeliverybranchid());
			kf.setEmaildate(dmpCwbOrder.getEmaildate());
			kf.setNextbranchid(dmpCwbOrder.getNextbranchid());
			kf.setOutbranchid(dmpCwbOrder.getStartbranchid());
			kf.setOutwarehousetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			kf.setPaybackfee(dmpCwbOrder.getPaybackfee());
			kf.setReceivablefee(dmpCwbOrder.getReceivablefee());
			kf.setCwb(dmpCwbOrder.getCwb());
			kuFangZaiTuDao.creKuFangZaiTu(kf);
		}
		saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.KuFangZaiTuTongJi.getText(),
				UpdateMessageMenuNameEnum.KuFangZaiTuTongJi.getValue());
	}

	/**
	 * 云数据迁移-处理更新各种操作时间和反馈的信息
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	public void updateAllTimeAndDeliveryForChange(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate, long functionid, long issycTime) {
		if (functionid == ChangeFunctionidEnum.TuoTouDingDanHuiZong.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe
						.getValue())) {// 处理妥投迁移
			try {
				this.saveDeliverySuccessful(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("妥投迁移异常", e);
			}

		}

		if (functionid == ChangeFunctionidEnum.ZhiLiuDingDanHuiZong.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) {// 分站滞留
																																										// 数据迁移
			try {
				this.saveDeliveryZhiLiu(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("分站滞留迁移异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.TuiHuoChuZhanTongJi.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {// 退货出站统计
			try {
				this.saveTuiHuoChuZhan(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("退货出站统计迁移异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.KuFangChuKuTongJi.getValue() && (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {// 处理库房出库迁移
			try {
				this.saveDeliveryChuKu(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("库房出库迁移异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.FenZhanDaoHuoTongJi.getValue()) {
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {// 处理分站到货迁移
				try {
					this.saveDeliveryDaohuo(orderFlow, dmpCwbOrder);
				} catch (Exception e) {
					logger.error("分站到货迁移异常", e);
				}
			} else {
				try {
					this.saveDeliveryDaohuoIsNow(orderFlow, dmpCwbOrder);
				} catch (Exception e) {
					logger.error("非到货更改到货表中的状态迁移异常", e);
				}
			}
		}

		if (functionid == ChangeFunctionidEnum.ZhongZhuanDingDanTongJi.getValue() && (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue())) {// 处理中转订单统计迁移
			try {
				this.saveZhongZhuan(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("中转订单统计迁移异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.JuShouDingDanHuiZong.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue())) {// 拒收订单汇总
																																										// 数据迁移
			try {
				this.saveDeliverJuShou(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("拒收订单汇总异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.TiHuoDingDanTongJi.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {// 提货订单统计
																																											// 数据迁移
			try {
				this.saveCwbTiHuo(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("提货订单统计异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.TuiHuoZhanRuKuTongJi.getValue() && orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			try {
				this.saveTuiHuoZhanRuKu(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("退货站入库统计迁移数据异常", e);
			}
		}

		if (functionid == ChangeFunctionidEnum.KuFangRuKuTongJi.getValue()
				&& (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.TiHuo
						.getValue())) {
			try {
				this.saveKuFangRuKuJi(orderFlow, dmpCwbOrder);
			} catch (Exception e) {
				logger.error("库房入库统计迁移数据异常", e);
			}

		}
		if (functionid == ChangeFunctionidEnum.KuFangZaiTuTongJi.getValue() && orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			try {
				this.saveKuFangZaiTuTongJi(orderFlow, dmpCwbOrder, deliverystate);
			} catch (Exception e) {
				logger.error("库房在途统计数据迁移异常", e);
			}
		}
		// Alan add start
		/**
		 * 保存每次操作
		 */
		if (functionid == 100) {
			try {
				Map<Long, Branch> branchNewMap = getBranchMap();
				Branch startbranch = branchNewMap.get(orderFlow.getBranchid());
				Branch nextbranch = branchNewMap.get(dmpCwbOrder.getNextbranchid()) == null ? new Branch() : branchNewMap.get(dmpCwbOrder.getNextbranchid());
				cwbOrderTailService.saveCwbOrderCurHandle(orderFlow, dmpCwbOrder, deliverystate, startbranch, nextbranch);
				saveUpdateMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), UpdateMessageMenuNameEnum.ZongHeChaXun.getText(),
						UpdateMessageMenuNameEnum.ZongHeChaXun.getValue());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("综合查询（云），数据迁移错误，flowfromjmsservice", e);
			}
		}
		// Alan add end
		if (issycTime == 1) {
			String cwb = orderFlow.getCwb();
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate());
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {// 中转站入库、出库
																																									// 时间处理
				Map<Long, Branch> branchNewMap = getBranchMap();
				Branch b = branchNewMap.get(orderFlow.getBranchid());
				if (b != null && b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {// 中转站
					if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
						cwbDAO.updateFlowTime(cwb, time, "zhongzhuanrukutime");
					} else {
						cwbDAO.updateFlowTime(cwb, time, "zhongzhuanzhanchukutime");
					}
					return;
				}
			}

			String flowTimeName = getFlowTimeName(orderFlow.getFlowordertype());
			if (!flowTimeName.equals("")) {// 处理更新操作时间
				cwbDAO.updateFlowTime(cwb, time, flowTimeName);
			} else {
				cwbDAO.updateFlowTime(cwb, time);
			}
		}

	}

	/**
	 * 获取操作类型的字段名称
	 * 
	 * @param flowordertype
	 * @return
	 */
	private String getFlowTimeName(int flowordertype) {
		for (FlowTimeEnum f : FlowTimeEnum.values()) {
			if (f.getValue() == flowordertype) {
				return f.getText();
			}
		}
		return "";
	}

	private CwbOrder setOrder(CwbOrder cwborder, DmpDeliveryState deliveryState) {
		if (deliveryState != null) {// 存储反馈表信息
			cwborder.setFdeliverid(deliveryState.getDeliveryid());
			cwborder.setReceivedfee(deliveryState.getReceivedfee());
			cwborder.setReturnedfee(deliveryState.getReturnedfee());
			cwborder.setBusinessfee(deliveryState.getBusinessfee());
			cwborder.setDeliverystate(deliveryState.getDeliverystate());
			cwborder.setCash(deliveryState.getCash());
			cwborder.setPos(deliveryState.getPos());
			cwborder.setPosremark(deliveryState.getPosremark());
			cwborder.setCheckfee(deliveryState.getCheckfee());
			cwborder.setCheckremark(deliveryState.getCheckremark());
			cwborder.setReceivedfeeuser(deliveryState.getReceivedfeeuser());
			cwborder.setCreatetime(deliveryState.getCreatetime());
			cwborder.setOtherfee(deliveryState.getOtherfee());
			cwborder.setPodremarkid(deliveryState.getPodremarkid());
			cwborder.setDeliverstateremark(deliveryState.getDeliverstateremark());
			cwborder.setSigninman(deliveryState.getSign_man());
			cwborder.setSignintime(deliveryState.getSign_time());
		}
		return cwborder;
	}

	/**
	 * 存储云功能数据迁移的最新时间
	 * 
	 * @param updatetime
	 * @param menuname
	 */
	public void saveUpdateMessage(String updatetime, String menuname, long menunvalue) {
		List<UpdateMessage> upList = updateMessageDAO.getUpdateMessageListByMenunvalue(menunvalue);
		if (upList != null && upList.size() > 0) {
			// 如果记录存在，就更新
			updateMessageDAO.saveUpdateMessageByMenunvalue(menunvalue, updatetime);
		} else {// 否则，插入
			updateMessageDAO.creUpdateMessage(menuname, updatetime, menunvalue);
		}
	}

	/**
	 * 订单修改
	 * 
	 * @param request
	 */
	public void editcwb(HttpServletRequest request) {
		long type = request.getParameter("type") == null ? 0 : Long.parseLong(request.getParameter("type").toString());
		String cwb = request.getParameter("cwb");
		if (type == 1) {// 重置审核状态
			long nextbranchid = request.getParameter("nextbranchid") == null ? 0 : Long.parseLong(request.getParameter("nextbranchid").toString());
			long flowordertype = request.getParameter("flowordertype") == null ? 0 : Long.parseLong(request.getParameter("flowordertype").toString());
			long currentbranchid = request.getParameter("currentbranchid") == null ? 0 : Long.parseLong(request.getParameter("currentbranchid").toString());
			long deliverystate = request.getParameter("deliverystate") == null ? 0 : Long.parseLong(request.getParameter("deliverystate").toString());
			// 订单表
			cwbDAO.updateForChongZhiShenHe(cwb, nextbranchid, flowordertype, currentbranchid, deliverystate);
			// 综合查询表
			cwbOrderTailDao.updateForChongZhiShenHe(cwb, nextbranchid, flowordertype, deliverystate);
			// 拒收订单
			deliveryJuShouDAO.delJuShouByCwb(cwb);
			// 滞留订单
			deliveryZhiLiuDAO.deleteDeliveryZhiLiu(cwb);
			// 妥投订单
			deliverySuccessfulDAO.deleteDeliverySuccessful(cwb);
		}
		if (type == 2) {// 修改订单金额
			BigDecimal receivablefee = new BigDecimal(request.getParameter("receivablefee") == null ? "0" : request.getParameter("receivablefee"));
			BigDecimal paybackfee = new BigDecimal(request.getParameter("paybackfee") == null ? "0" : request.getParameter("paybackfee"));
			// 订单表
			cwbDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 综合查询表
			cwbOrderTailDao.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 拒收订单
			deliveryJuShouDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 滞留订单
			deliveryZhiLiuDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 妥投订单
			deliverySuccessfulDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 库房出库统计
			deliveryChukuDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 分站到货统计
			deliveryDaohuoDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 中转订单统计
			zhongZhuanDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 退货站入库
			tuiHuoZhanRuKuDao.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 库房入库
			kuFangRuKuDao.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 库房在途订单
			kuFangZaiTuDao.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 提货订单
			cwbTiHuoDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
			// 库对库出库
			kdkDeliveryChukuDAO.updateXiuGaiJinE(cwb, receivablefee, paybackfee);
		}
		if (type == 3) {// 修改订单支付方式
			int newpaywayid = request.getParameter("newpaywayid") == null ? 0 : Integer.parseInt(request.getParameter("newpaywayid").toString());
			// 订单表
			cwbDAO.updateXiuGaiZhiFuFangShi(cwb, newpaywayid);
			// 综合查询表
			cwbOrderTailDao.updateXiuGaiZhiFuFangShi(cwb, newpaywayid);
			// 妥投订单
			deliverySuccessfulDAO.updateXiuGaiZhiFuFangShi(cwb, newpaywayid);
		}
		if (type == 4) {// 修改订单类型
			int cwbordertypeid = request.getParameter("cwbordertypeid") == null ? 0 : Integer.parseInt(request.getParameter("cwbordertypeid").toString());
			long deliverystate = request.getParameter("deliverystate") == null ? 0 : Long.parseLong(request.getParameter("deliverystate").toString());
			// 订单表
			cwbDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid, deliverystate);
			// 综合查询表
			cwbOrderTailDao.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid, deliverystate);
			// 拒收订单
			deliveryJuShouDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid, deliverystate);
			// 滞留订单
			deliveryZhiLiuDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 妥投订单
			deliverySuccessfulDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid, deliverystate);
			// 库房出库统计
			deliveryChukuDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 分站到货统计
			deliveryDaohuoDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 中转订单统计
			zhongZhuanDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 退货站入库
			tuiHuoZhanRuKuDao.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 退货出站
			tuiHuoChuZhanDao.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 库房入库
			kuFangRuKuDao.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 库房在途订单
			kuFangZaiTuDao.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 提货订单
			cwbTiHuoDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
			// 库对库出库
			kdkDeliveryChukuDAO.updateXiuGaiDingDanLeiXing(cwb, cwbordertypeid);
		}
	}

}
