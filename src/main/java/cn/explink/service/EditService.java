package cn.explink.service;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExcelImportEditDao;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExcelImportEdit;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.support.transcwb.OneTransToMoreCwbDao;
import cn.explink.support.transcwb.TransCwbDao;

@Service
public class EditService {
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
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExcelImportEditDao excelImportEditDao;

	private ObjectMapper om = JacksonMapper.getInstance();
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.editshowinfo.orderFlow";
	
	private static final String MQ_FROM_URI_CWBBATCH_DELETE = "jms:queue:VirtualTopicConsumers.editdeleteinfo.cwbbatchDelete";
	
	private static final String MQ_FROM_URI_LOSE_CWB = "jms:queue:VirtualTopicConsumers.editdeleteinfoTwo.loseCwb";

	@PostConstruct
	public void init() {
		try {
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI_ORDER_FLOW + "?concurrentConsumers=5").to("bean:editService?method=saveEdit").routeId("editSave");
					this.from(MQ_FROM_URI_CWBBATCH_DELETE + "?concurrentConsumers=5").to("bean:editService?method=deleteEdit").routeId("editDelete");
					this.from(MQ_FROM_URI_LOSE_CWB + "?concurrentConsumers=5").to("bean:editService?method=deleteEditTwo").routeId("editDeleteTwo");

				}
			});
		} catch (Exception e) {
			this.logger.error("camel context start fail", e.getMessage());
			e.printStackTrace();
		}

	}

	public void deleteEditTwo(@Header("loseCwbByEmaildateid") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		this.logger.info("--edit订单失效功能，订单号：{}", parm);
		try {
			this.logger.info("RE:jms:queue:VirtualTopicConsumers.editService.editloseCwb ");
			JSONObject emaildateidJSON = JSONObject.fromObject(parm);
			int emaildateid = emaildateidJSON.getInt("emaildateid");
			this.excelImportEditDao.deleteByEmaildate(emaildateid);

		} catch (Exception e) {
			this.logger.info("--edit订单失效功能：信息：{}", parm);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".deleteEditTwo")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_LOSE_CWB)
					.buildMessageHeader("loseCwbByEmaildateid", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	public void deleteEdit(@Header("losecwbbatch") String cwb, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		this.logger.info("--edit订单失效功能，订单号：{}", cwb);
		try {
			if (cwb.trim().length() > 0) {
				this.excelImportEditDao.deleteEdit(cwb);
			}
		} catch (Exception e) {
			this.logger.info("--edit订单失效功能：，订单号：{}", cwb);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".deleteEdit")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_CWBBATCH_DELETE)
					.buildMessageHeader("losecwbbatch", cwb)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	public void saveEdit(@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			OrderFlow orderFlow = this.om.readValue(parm, OrderFlow.class);
			this.saveEdit(orderFlow);
		} catch (Exception e) {
			// TODO handle exception
			this.logger.error("saveEdit failed. parm = " + parm);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".saveEdit")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_ORDER_FLOW)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	@Transactional
	public void saveEdit(OrderFlow orderFlow) {
		try {
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()
					|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
					|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
					|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				// 删除edit表
				this.excelImportEditDao.deleteEdit(orderFlow.getCwb());
				return;
			}
			if (orderFlow.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue() && orderFlow.getFlowordertype() != FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
				return;
			}
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.om.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder dmpcwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			if (dmpcwbOrder == null) {
				this.logger.warn("消息不完整，没有订单信息,id:", orderFlow.getFloworderid());
				return;
			}
			/*Customer c = this.customerDAO.getCustomerById(dmpcwbOrder.getCustomerid());
			if (c.getIsUsetranscwb() == 2) {// 如果不扫描运单，不保存订单和运单对应用表
				this.logger.info("该供货商不使用扫描运单，订单号:{},供货商：{}", orderFlow.getCwb(), c.getCustomername());
				return;
			}*/
			// 如果不区分大小写先将订单号的大小写存入
			this.logger.info("订单号{},站点{}", dmpcwbOrder.getCwb(), dmpcwbOrder.getDeliverybranchid());
			ExcelImportEdit e = this.excelImportEditDao.getEditInfoByCwb(dmpcwbOrder.getCwb());
			if (e == null) {
				this.excelImportEditDao.insertEditInfo(dmpcwbOrder);
			} else {
				this.logger.info("更新edit表");
				this.excelImportEditDao.updateEditInfo(dmpcwbOrder);
			}

		} catch (Exception e) {
			this.logger.info("异常" + e.getMessage());
		}
	}
}
