package cn.explink.b2c.zhts;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.explink.core_down.EpaiApi;
import cn.explink.b2c.explink.core_down.EpaiApiDAO;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.weisuda.xml.ObjectUnMarchal;
import cn.explink.b2c.zhts.xmldto.Order;
import cn.explink.b2c.zhts.xmldto.OrderTrack;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ZhtsOrderTrackService {
	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	UserDAO userDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.orderTrack.orderFlow";
	
	/**
	 * 全流程监听
	 * 
	 * @param orderFlow
	 */
	@Consume(uri = MQ_FROM_URI_ORDER_FLOW + "?concurrentConsumers=5")
	public void orderTrack(@Header("orderFlow") String orderFlow, @Header("MessageHeaderUUID") String messageHeaderUUID){
		String cwb="";
		try{

			OrderFlow orderFlowObj =JacksonMapper.getInstance().readValue(orderFlow, OrderFlow.class);
			
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = JacksonMapper.getInstance().readValue(orderFlowObj.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			
			CwbOrder cwbOrder =cwbOrderWithDeliveryState.getCwbOrder();
			
			long customerid=cwbOrder.getCustomerid();
			
			EpaiApi epaiApi=epaiApiDAO.getEpaiApiByKey(customerid);
			if(epaiApi==null||epaiApi.getOpen_ordertrackflag()==0){
				return;
			}
			
			if(!filterOrderTrackFlow(orderFlowObj)){
				return;
			}
			
			cwb=cwbOrder.getCwb();
			
			OrderTrack orderTrack = buildOrderTrack(orderFlowObj, cwbOrder);
			
			String requestTime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
			String sign = MD5Util.md5(epaiApi.getUserCode()+requestTime+epaiApi.getPrivate_key());
			
			String xml = ObjectUnMarchal.POJOtoXml(orderTrack);
			
			Map<String,String> paraMap = new HashMap<String, String>();
			paraMap.put("userCode", epaiApi.getUserCode());
			paraMap.put("requestTime", requestTime);
			paraMap.put("sign", sign);
			paraMap.put("content", xml);
			
			String responseXml = RestHttpServiceHanlder.sendHttptoServer(paraMap, epaiApi.getOrdertrack_url());
			
			logger.info("中浩轨迹返回:{}",responseXml);
		}catch (Exception e){
			this.logger.error("推送中浩途胜全流程异常,cwb="+cwb, e);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".orderTrack")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_ORDER_FLOW)
					.buildMessageHeader("orderFlow", orderFlow)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	private boolean filterOrderTrackFlow(OrderFlow orderFlowObj) {
		for(OrderTrackFlowEnum enums:OrderTrackFlowEnum.values()){
			if(enums.getValue()==orderFlowObj.getFlowordertype()){
				return true;
			}
		}
		return false;
	}

	private OrderTrack buildOrderTrack(OrderFlow orderFlowObj, CwbOrder cwbOrder) {
		OrderTrack orderTrack = new OrderTrack();
		Order order = new Order();
		order.setOrderNo(cwbOrder.getCwb());
		order.setTransOrderNo(cwbOrder.getTranscwb());
		order.setOperationTime(DateTimeUtil.formatDate(orderFlowObj.getCredate()));
		order.setOperationTrack(explinkService.getDetail(orderFlowObj));
		order.setOperatorName(userDAO.getUserByidAdd(orderFlowObj.getUserid()).getRealname());
		orderTrack.setOrder(order);
		return orderTrack;
	}
}
