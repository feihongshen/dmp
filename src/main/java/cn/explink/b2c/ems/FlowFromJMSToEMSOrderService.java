package cn.explink.b2c.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.StringUtil;
/*import cn.explink.dao.MqExceptionDAO;*/
import cn.explink.util.Tools;

/**
 * 监听流程产生的JMS消息，用于存储EMS订单下发数据
 */
@Service
public class FlowFromJMSToEMSOrderService {
	@Autowired
	private CamelContext camelContext;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	EMSDAO eMSDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	EMSService eMSService;
	@Autowired
	MqExceptionDAO mqExceptionDAO;
	@Autowired
	JointService jointService;
	/*@Produce(uri = "jms:queue:sendBToCToDmp")
	ProducerTemplate sendBToCToDmpProducer;*/

	private ObjectMapper objectMapper = new ObjectMapper();
	/*private ObjectReader dmpOrderReader = this.objectMapper.reader(DmpCwbOrder.class);*/

	private Logger logger = LoggerFactory.getLogger(FlowFromJMSToEMSOrderService.class);
	private List<Integer> flowList = new ArrayList<Integer>(); // 存储 对接所用的环节
	
	/*@Autowired
	private MqExceptionDAO mqExceptionDAO;*/
	
	private static final String MQ_FROM_URI = "jms:queue:VirtualTopicConsumers.ems.orderFlow";

	@PostConstruct
	public void init() {
		try {

			this.flowList.add(FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()); // 更改配送站
			this.flowList.add(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()); // 出库扫描
			this.flowList.add(FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()); // 中转站出库
			this.flowList.add(CwbFlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue()); // 退货站再投扫描
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {

					this.from(MQ_FROM_URI + "?concurrentConsumers=20").to("bean:flowFromJMSToEMSOrderService?method=saveOrderForEms").routeId("ems_");
				}
			});

		} catch (Exception e) {
			this.logger.error("EMS订单下发接口监听JMS异常！" + e);
		}
	}

	public void saveOrderForEms(@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			return;
		}
		try {
			doSaveOrderForEms(parm,ems.getEmsBranchid());
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".saveFlowB2cSend")
					.buildExceptionInfo(e1.toString()).buildTopic(MQ_FROM_URI)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}
	
	//接收环节消息，用于EMS信息存储 表:express_ems_order_b2ctemp
	public void  doSaveOrderForEms(String parm,long branchid)throws Exception{
		
		try {
			OrderFlow orderflow = this.objectMapper.readValue(parm, OrderFlow.class);
			int floworderType = orderflow.getFlowordertype();
			if (!this.flowList.contains(floworderType)) {
				this.logger.warn("RE:cwb=" + orderflow.getCwb() + ",flowordertype=" + orderflow.getFlowordertype());
				return;
			}
			
			//校验订单是否重复插入
			int resaveFlag = validateOrderResave(orderflow.getCwb());
			
			if(resaveFlag==1){
				this.logger.info("ems订单下发接口临时表中已存在该订单对应的数据：订单号为：{}",orderflow.getCwb());
				return;
			}
			CwbOrder order = this.cwbDAO.getCwbByCwb(orderflow.getCwb());
			if(order==null){
				this.logger.info("dmp订单号不存在，订单号为：{}",orderflow.getCwb());
				return;
			}
			//配送站为非ems的订单不发送给ems
			if((floworderType==6&&order.getNextbranchid()!=branchid)
					|| (floworderType==14 && order.getNextbranchid()!=branchid)
					|| (floworderType==17 && order.getNextbranchid()!=branchid)
					|| (floworderType==37 && order.getDeliverybranchid()!=branchid)){
				return;
			}
			
			//应收运费和应收金额均大于0的订单不发送给ems
			if(order.getReceivablefee().compareTo(BigDecimal.ZERO)>0&&order.getShouldfare().compareTo(BigDecimal.ZERO)>0){
				this.logger.info("应收运费和应收金额均大于0的订单不发送给ems，订单号为：{}",orderflow.getCwb());
				return;
			}
			
			//集单的数据不发送给EMS
			if(order.getIsmpsflag() == IsmpsflagEnum.yes.getValue()){
				return;
			}
			
			this.logger.info("保存EMS订单消息开始：" + System.currentTimeMillis());
			this.logger.info("EMS订单保存， 环节信息处理,{}", parm);
			
			int sendnum =Integer.parseInt(order.getSendcarnum()+"");
			String[] arrTranscwb = new String[sendnum];
			String transcwbstr = order.getTranscwb();
			int addTranscwbFlag = 0; 
			//不同运单号之间用逗号分隔的运单号字符串
			String addTranscwbs = "";
			
			if(StringUtil.isEmpty(transcwbstr)){
				StringBuffer transcwbs = new StringBuffer();
				//订单号不存在运单号的时候，根据订单的发货件数生成运单号插入ems接口临时表，和dmp运单、订单对照表
				if(sendnum != 0){
					//根据发货件数生成同等数量的运单号
					for(int i=0;i<sendnum;i++){
						arrTranscwb[i] = order.getCwb()+"-ems"+(i+1);
						transcwbs.append(arrTranscwb[i]).append(",");
					}
					addTranscwbFlag = 1;
					addTranscwbs = transcwbs.toString().substring(0, transcwbs.length()-1);
				}else{
					this.logger.info("dmp订单数据异常，发货件数为0，订单号为：{}",orderflow.getCwb());
					return;
				}
			}else{
				String strSplit = this.getSplitstring(transcwbstr);
				arrTranscwb = transcwbstr.split(strSplit);
			}
			
			String credate = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			
			for(String transcwb : arrTranscwb){
				if(transcwb == null || transcwb.trim().equals("")){
					continue;
				}
				//处理订单信息，将dmp订单信息解析成发送给ems的数据格式
				String data = getStringToEMS(order,transcwb);
				
				if(StringUtil.isEmpty(data)){
					return;
				}
				List<SendToEMSOrder> oldOrder = eMSDAO.getSendOrderByTranscwb(transcwb);
				if(oldOrder.size()!=0){
					this.logger.info("发送给ems的对应数据在接口临时表中已存在，运单号为：{}",transcwb);
					return;
				}
				//每个运单号对应临时表中一条记录 edit by zhouhuan 2016-07-21
				eMSDAO.saveOrderInfo(orderflow.getCwb(),transcwb,credate,addTranscwbFlag,data,0l);
				List<TranscwbView> transcwbList = transCwbDao.getcwbBytranscwb(transcwb);
				
				if(transcwbList.size()==0){
					//插入运单、订单对照表
					transCwbDao.saveTranscwb(transcwb,orderflow.getCwb());
				}
			}
			if(StringUtil.isEmpty(transcwbstr)){
				//将新增的运单号更新到订单表中
				cwbDAO.updateTranscwbByCwb(orderflow.getCwb(), addTranscwbs);
			}
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			throw e1;
		}
		this.logger.info("保存EMS订单消息结束：" + System.currentTimeMillis());
	}
	
	//将dmp订单信息解析为发送的xml格式
	private String getStringToEMS(CwbOrder order,String transcwb) {
		List<EMSOrderInfo> list = new ArrayList<EMSOrderInfo>();
		EMSOrderInfo eMSOrderInfo = new EMSOrderInfo();
		eMSOrderInfo.setBigAccountDataId(transcwb);
		/*
		 * 业务类型为：
		 * 1:标准快递，应收金额为0且应收运费为0的配送类型、快递类型、OXO订单,
		 * 2:代收货款，应收金额>0的配送类型、快递类型、OXO订单
		 * 3:收件人付费	应收运费>0的配送类型、快递类型、OXO订单
		 */
		if((order.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.Express.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.OXO.getValue())
				&& order.getReceivablefee().compareTo(BigDecimal.ZERO)==0 && order.getShouldfare().compareTo(BigDecimal.ZERO)==0){
			eMSOrderInfo.setBusinessType("1");
		}else if((order.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.Express.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.OXO.getValue())
				&& order.getReceivablefee().compareTo(BigDecimal.ZERO)>0){
			eMSOrderInfo.setBusinessType("2");
		}else if((order.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.Express.getValue()||order.getCwbordertypeid()==CwbOrderTypeIdEnum.OXO.getValue())
				&& order.getShouldfare().compareTo(BigDecimal.ZERO)>0){
			eMSOrderInfo.setBusinessType("3");
		}else if(order.getReceivablefee().compareTo(BigDecimal.ZERO)>0&& order.getShouldfare().compareTo(BigDecimal.ZERO)>0){
			this.logger.info("应收运费和应收金额均大于0的订单不发送给ems：{}" ,order.getCwb());
			return null;
		}else{
			this.logger.info("订单数据不符合ems业务类型要求,订单号为：{}" ,order.getCwb());
			return null;
		}
		//邮件号
		eMSOrderInfo.setBillno(order.getCwb());
	    //时间类型
		eMSOrderInfo.setDateType("1");
		//寄件人姓名
		if(!StringUtil.isEmpty(order.getSendername())){
			eMSOrderInfo.setScontactor(order.getSendername());
		}
		//寄件人电话1
		eMSOrderInfo.setScustMobile("13*****0000");
		//寄件人地址
		if(order.getCwbstate()==CwbOrderTypeIdEnum.Express.getValue()){
			eMSOrderInfo.setScustAddr(order.getSenderstreet());
		}
		//收件人姓名
		if(!StringUtil.isEmpty(order.getConsigneename())){
			eMSOrderInfo.setTcontactor(order.getConsigneename());
		}
		//收件人电话
		if(!StringUtil.isEmpty(order.getConsigneemobile())){
			eMSOrderInfo.setTcustMobile(order.getConsigneemobile());
		}
		//收件人地址
		if(!StringUtil.isEmpty(order.getConsigneeaddress())){
			eMSOrderInfo.setTcustAddr(order.getConsigneeaddress());
		}
		//货物重量
		eMSOrderInfo.setWeight(order.getCarrealweight());
		//货款金额:当业务类型为代收货款时：取应收金额；当业务类型为收件人付费时：取应收运费
		if(eMSOrderInfo.getBusinessType().equals("2")){
			eMSOrderInfo.setFee(order.getReceivablefee());
		}else if(eMSOrderInfo.getBusinessType().equals("3")){
			eMSOrderInfo.setFee(order.getShouldfare());
		}
		eMSOrderInfo.setCustomerDn(transcwb);
		//ems校验字段给只
		/*eMSOrderInfo.setProcdate(Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		eMSOrderInfo.setLength(BigDecimal.ZERO);
		eMSOrderInfo.setInsure(BigDecimal.ZERO);
		eMSOrderInfo.setInsurance(BigDecimal.ZERO);
		eMSOrderInfo.setSubBillCount(order.getSendnum());
		eMSOrderInfo.setMainBillFlag(3+"");
		eMSOrderInfo.setMainSubPayMode(1+"");
		eMSOrderInfo.setInsureType(2+"");
		eMSOrderInfo.setPayMode(1+"");*/
		
		list.add(eMSOrderInfo);
		String datastr = Tools.getObjectToXml(list);
		
		return datastr;
	}

	//校验订单是否重复插入
	public int validateOrderResave(String cwb){
		int flag = 0;
		List<SendToEMSOrder> list = eMSDAO.getOrderInfoByTranscwb(cwb);
		if(list.size()!=0){
			flag=1;
		}
		return flag;
	}
	
	public String getSplitstring(String transcwb) {
		if (transcwb.indexOf(':') != -1) {
			return ":";
		}
		return ",";
	}
	
}
