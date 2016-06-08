package cn.explink.support.transcwb;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.CwbTranslator;

@Service
public class TransCwbService implements CwbTranslator {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CamelContext camelContext;

	@Autowired
	TransCwbDao transCwbDao;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OneTransToMoreCwbDao oneTransToMoreCwbDao;

	private ObjectMapper om = JacksonMapper.getInstance();
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.transcwb.orderFlow";

	@PostConstruct
	public void init() {
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from(MQ_FROM_URI_ORDER_FLOW + "?concurrentConsumers=5").to("bean:transCwbService?method=saveTransCwb").routeId("运单号保存");
				}
			});
		} catch (Exception e) {
			logger.error("camel context start fail", e);
		}

	}

	public void saveTransCwb(@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		logger.debug("orderFlow 运单号处理,{}", parm);
		try {
			OrderFlow orderFlow = om.readValue(parm, OrderFlow.class);
			if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return;
			}

			saveTransCwb(orderFlow);
		} catch (Exception e) {
			logger.error("处理运单号出错", e);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".saveTransCwb")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_ORDER_FLOW)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	public void saveTransCwb(OrderFlow orderFlow) throws IOException, JsonParseException, JsonMappingException {
		SystemInstall transcwbSupport = systemInstallDAO.getSystemInstallByName("transcwbSupport");
		if (transcwbSupport == null || !transcwbSupport.getValue().equals("true")) {
			return;
		}

		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = om.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
		CwbOrder dmpcwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
		if (dmpcwbOrder == null) {
			logger.warn("消息不完整，没有订单信息,id:", orderFlow.getFloworderid());
			return;
		}
		Customer c = customerDAO.getCustomerById(dmpcwbOrder.getCustomerid());
		if (c.getIsUsetranscwb() == 2) {// 如果不扫描运单，不保存订单和运单对应用表
			logger.info("该供货商不使用扫描运单，订单号:{},供货商：{}", orderFlow.getCwb(), c.getCustomername());
			return;
		}
		// 如果不区分大小写先将订单号的大小写存入
		if (c.getIsqufendaxiaoxie() == 0) {
			boolean isLetter = false;
			for (int i = 0; i < dmpcwbOrder.getCwb().length(); i++) {
				if (Character.isLetter(dmpcwbOrder.getCwb().charAt(i))) {
					isLetter = true;
				}
			}
			if (isLetter) {
				transCwbDao.saveTranscwb(dmpcwbOrder.getCwb().toLowerCase(), dmpcwbOrder.getCwb());
				transCwbDao.saveTranscwb(dmpcwbOrder.getCwb().toUpperCase(), dmpcwbOrder.getCwb());
			}
		}
		if ((B2cEnum.Amazon.getKey() + "").equals(c.getB2cEnum())) {// 属于亚马逊的
			try {
				if (dmpcwbOrder.getTranscwb() != null && !"".equals(dmpcwbOrder.getTranscwb().trim())) {
					oneTransToMoreCwbDao.saveTranscwb(dmpcwbOrder.getTranscwb(), dmpcwbOrder.getCwb());
				}
			} catch (Exception e) {
				try {
					oneTransToMoreCwbDao.updateTranscwb(dmpcwbOrder.getTranscwb(), dmpcwbOrder.getCwb());
					logger.error("[亚马逊]插入set_onetranscwb_to_morecwbs 表异常，主键重复。已更新transcwb");
				} catch (Exception e1) {
					logger.error("", e1);
				}
			}
		}
		String multitranscwb = dmpcwbOrder.getTranscwb();
		for (String transcwb : multitranscwb.split(cwbOrderService.getSplitstring(multitranscwb))) {
			try {
				if (!StringUtils.hasLength(transcwb)) {
					continue;
				}
				if (c.getIsqufendaxiaoxie() == 0) {
					boolean isLetter = false;
					for (int i = 0; i < transcwb.length(); i++) {
						if (Character.isLetter(transcwb.charAt(i))) {
							isLetter = true;
						}
					}
					if (isLetter) {
						transCwbDao.saveTranscwb(transcwb.toLowerCase(), dmpcwbOrder.getCwb());
						transCwbDao.saveTranscwb(transcwb.toUpperCase(), dmpcwbOrder.getCwb());
					}
				}
				transCwbDao.saveTranscwb(transcwb, dmpcwbOrder.getCwb());
			} catch (DataAccessException e) {
				logger.error("存储运单号出错", e);
			}
		}
	}

	public void saveTransCwbByFloworderdetail(String floworderdetail) {
		// {"cwbOrder":{"opscwbid":21810,"startbranchid":0,"currentbranchid":0,"nextbranchid":189,"deliverybranchid":192,"backtocustomer_awb":"","cwbflowflag":"1","carrealweight":25.000,"cartype":"家电","carwarehouse":"189","carsize":"11*10*10","backcaramount":0.00,"sendcarnum":1,"backcarnum":0,"caramount":11111.11,"backcarname":"取回商品T1","sendcarname":"发出商品T1","deliverid":0,"deliverystate":0,"emailfinishflag":0,"reacherrorflag":0,"orderflowid":0,"flowordertype":1,"cwbreachbranchid":0,"cwbreachdeliverbranchid":0,"podfeetoheadflag":"0","podfeetoheadtime":null,"podfeetoheadchecktime":null,"podfeetoheadcheckflag":"0","leavedreasonid":0,"deliversubscribeday":null,"customerwarehouseid":"4","emaildateid":258,"emaildate":"2013-07-09 09:50:54","serviceareaid":4,"customerid":126,"shipcwb":"LG123456789","consigneeno":"CustomerID1","consigneename":"王旸","consigneeaddress":"中国北京北京市朝阳区,建国门外大街甲12号新华保险大厦10层","consigneepostcode":"100022","consigneephone":"15901169883","cwbremark":"备注T1","customercommand":"客户要求T1","transway":"航空","cwbprovince":"北京","cwbcity":"北京市","cwbcounty":"朝阳区","receivablefee":11111.11,"paybackfee":0.00,"cwb":"7901","shipperid":0,"cwbordertypeid":1,"consigneemobile":"15901169883","transcwb":"LG123456789","destination":"北京","cwbdelivertypeid":"1","exceldeliver":"","excelbranch":"朝阳站","excelimportuserid":1001,"state":1,"printtime":"","commonid":5,"commoncwb":"","signtypeid":0,"podrealname":"","podtime":"","podsignremark":"","modelname":null,"scannum":0,"isaudit":0,"backreason":"","leavedreason":"","paywayid":1,"newpaywayid":"1","tuihuoid":189,"cwbstate":1,"remark1":"自定义1T1","remark2":"自定义2T1","remark3":"自定义3T1","remark4":"自定义4T1","remark5":"自定义5T1","backreasonid":0,"multi_shipcwb":null,"packagecode":"","backreturnreasonid":0,"backreturnreason":"","handleresult":0,"handleperson":0,"handlereason":"","addresscodeedittype":0},"deliveryState":null}
		logger.info("orderFlow 调接口运单号处理,{}", floworderdetail);
		try {
			SystemInstall transcwbSupport = systemInstallDAO.getSystemInstallByName("transcwbSupport");
			if (transcwbSupport == null || !transcwbSupport.getValue().equals("true")) {
				return;
			}
			String cwb = floworderdetail.substring(floworderdetail.indexOf("\"cwb\":\"") + 7, floworderdetail.indexOf("\",\"shipperid\""));
			String multitranscwb = floworderdetail.substring(floworderdetail.indexOf("\"transcwb\":\"") + 12, floworderdetail.indexOf("\",\"destination\""));
			String customerid = floworderdetail.substring(floworderdetail.indexOf("\"customerid\":") + 13, floworderdetail.indexOf(",\"shipcwb\""));
			if (multitranscwb.length() == 0) {
				return;
			}
			Customer c = customerDAO.getCustomerById(Long.parseLong(customerid));
			if (c.getIsUsetranscwb() == 2) {// 如果不扫描运单，不保存订单和运单对应用表
				logger.info("该供货商不使用扫描运单，订单号:{},供货商：{}", cwb, c.getCustomername());
				return;
			}
			for (String transcwb : multitranscwb.split(cwbOrderService.getSplitstring(multitranscwb))) {
				try {
					if (!StringUtils.hasLength(transcwb)) {
						return;
					}
					transCwbDao.saveTranscwb(transcwb, cwb);
				} catch (DataAccessException e) {
					logger.error("存储运单号出错", e);
				}
			}
		} catch (Exception e) {
			logger.error("处理运单号出错", e);
		}
	}

	@Override
	public String translate(String incomecwb) {
		return transCwbDao.getCwbByTransCwb(incomecwb);
	}
}
