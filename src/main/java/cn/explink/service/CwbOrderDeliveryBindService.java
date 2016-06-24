package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.JsonUtil;

/**
 * 订单绑定/解绑小件员同步到tps服务类  2016-06-20
 *
 */

@Service
public class CwbOrderDeliveryBindService {
	
	@Autowired
	private CamelContext camelContext;
	
	@Autowired
	CwbDAO cwbDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MQ_FROM_URI = "jms:queue:VirtualTopicConsumers.cwbbind.orderFlow";

	private List<Integer> flowList = new ArrayList<Integer>(); // 存储需要的环节
	
	@PostConstruct
	public void init() {
		try {

			this.flowList.add(FlowOrderTypeEnum.YiFanKui.getValue()); // 反馈
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI + "?concurrentConsumers=10").to("bean:cwbOrderDeliveryBindService?method=cwbDeliveryBind").routeId("cwbbind_");
				}
			});

		} catch (Exception e) {
			this.logger.error("订单绑定/解绑小件员同步到tps 监听JMS异常！" + e);
		}
	}
	
	public void cwbDeliveryBind (@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			doCwbDeliveryBind(parm);
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".cwbDeliveryBind")
					.buildExceptionInfo(e1.toString()).buildTopic(MQ_FROM_URI)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}
	
	private void doCwbDeliveryBind(String parm) {
		try {
			OrderFlow orderFlow = JsonUtil.readValue(parm, OrderFlow.class);
			int floworderType = orderFlow.getFlowordertype();
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = JsonUtil.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			if (!this.flowList.contains(floworderType)) {
				this.logger.warn("订单绑定小件员，忽略订单操作状态"+orderFlow.getFlowordertype()+"，订单号："+orderFlow.getCwb());
				return;
			}
			List<CwbOrder> shangMenTuiCwbList = cwbDAO.queryRelatedShangMenTuiCwbList(cwbOrderWithDeliveryState.getCwbOrder().getCwb());
			if (shangMenTuiCwbList != null && !shangMenTuiCwbList.isEmpty()) {
				for (CwbOrder co : shangMenTuiCwbList) {
					if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {//配送关联揽退单已领货
						//TODO
						logger.info("调用do 订单号：" + orderFlow.getCwb());
					}
				}
			}
		
		} catch (Exception e) {
			logger.error("订单绑定/解绑小件员同步到tps 发生异常", e);
		}
		
	}
		
}
