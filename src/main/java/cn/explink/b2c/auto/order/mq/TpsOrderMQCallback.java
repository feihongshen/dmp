package cn.explink.b2c.auto.order.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.explink.b2c.auto.order.constant.BusinessType;
import cn.explink.b2c.auto.order.handle.IOrderHandler;
import cn.explink.b2c.auto.order.service.AutoExceptionService;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;

import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.VMSEventArgs;

/**
 * tms订单下发消费处理
 * 正常订单、揽退单、OXO单、快递单都从这里走
 * @author jian.xie
 *
 */
@Component("tpsOrderMQCallback")
public class TpsOrderMQCallback implements IVMSCallback {
	protected Logger logger = LoggerFactory.getLogger(TpsOrderMQCallback.class);
	
	@Autowired
	AutoExceptionService autoExceptionService;
	
	@Autowired
	AutoExceptionSender autoExceptionSender;
	
	@Autowired
	@Qualifier("tpsOrderConsumer")
	private ConsumerTemplate consumerTemplate;
	
	@Autowired
	@Qualifier("peisongOrderHandler")
	IOrderHandler peisongOrderHandler;
	
	@Autowired
	@Qualifier("shangmentuiOrderHandler")
	IOrderHandler shangmentuiOrderHandler;
	
	@Autowired
	@Qualifier("oXOOrderHandler")
	IOrderHandler oXOOrderHandler;
	
	@Autowired
	@Qualifier("expressOrderHandler")
	IOrderHandler expressOrderHandler;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void onSuccess(Object sender, VMSEventArgs e) {
		VipShop vipshop = null;
		String msg = null;
    	IOrderHandler orderHandler = null;
		try {
			msg = new String(e.getPayload(), "utf-8");
			this.logger.info("TPS订单下发接口报文：" + msg);			
			InfDmpOrderSendVO orderSend = objectMapper.readValue(msg, InfDmpOrderSendVO[].class)[0];
			// 效验数据
			if(!verify(orderSend, msg)){
				return;
			};
			orderHandler = getOrderHandler(orderSend);
			if(orderHandler == null){
				feedbackException(msg, "传入business_type不正确", orderSend.getCustOrderNo());
			}
			orderHandler.dealWith(orderSend);			
		} catch (Exception ex) {
			this.logger.error("消费TPS订单下发数据时解析异常!", ex);
			ex.printStackTrace();
			feedbackException(msg, ex.getMessage(), null);
		} finally {
			// 确认消费
			ISubscriber subscriber = (ISubscriber) sender;
			subscriber.commit();
		}
	}

	/**
	 * 发送异常信息到MQ
	 * @param msg 报文内容
	 * @param exceptionInfo  异常信息
	 * @param cwb TODO
	 */
	private void feedbackException(String msg, String exceptionInfo, String cwb) {		
		logger.info("tps订单下发异常:单号{},详情{}", cwb, exceptionInfo);
		long msgid = this.autoExceptionService.createAutoExceptionMsg(msg, AutoInterfaceEnum.dingdanxiafa.getValue());
		long detailId = this.autoExceptionService.createAutoExceptionDetail(cwb, "", "TPS自动化订单下发数据异常", AutoExceptionStatusEnum.xinjian.getValue(), msgid, 0, "");
		AutoMQExceptionDto mqe = new AutoMQExceptionDto();
		mqe.setBusiness_id("");
		mqe.setException_info(exceptionInfo);
		mqe.setMessage(msg);
		mqe.setRefid(detailId);
		String sendXml = StringXMLSend(mqe, msg);
		autoExceptionSender.send(sendXml);
	}

	/**
	 * 效验数据
	 * @param orderSend
	 * @return 
	 */
	private boolean verify(InfDmpOrderSendVO orderSend, String msg) {
		if(orderSend == null){
			feedbackException(msg, "报文反序列结果为空", "报文出错");
			return false;
		}
		
		
		return true;
	}

	@Override
	public void onFailure(Object sender, VMSEventArgs e, Throwable cause) {
		String forRecover = "";
        this.logger.info("消费tps下发订单，异常信息为：" + cause.toString());
        try {
            forRecover = new String(e.getPayload(), "utf-8");
        } catch (Throwable e1) {
            this.logger.error("消费tps下发订单，onFailure：" + e1.toString());
            autoExceptionSender.send(forRecover); 
        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
	}
	
	//异常报文返回数据拼接
	private String StringXMLSend(AutoMQExceptionDto error, String falure) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String date = String.valueOf(df.format(new Date()));
		StringBuilder sub = new StringBuilder();
		sub.append("<root>");
		sub.append("<system_name>ZDH_Order</system_name>");
		sub.append("<queue_name>" + consumerTemplate.getQueueName() + "</queue_name>");
		sub.append("<exchange_name>" + consumerTemplate.getExchangeName() + "</exchange_name>");
		sub.append("<routing_key>*</routing_key>");
		sub.append("<exception_info>" + error.getException_info() + "</exception_info>");
		sub.append("<business_id></business_id>");
		sub.append("<create_time>" + date + "</create_time>");
		sub.append("<remark></remark>");
		sub.append("<message><![CDATA[" + falure + "]]></message>");
		sub.append("</root>");
		return sub.toString();
	}
	
    private IOrderHandler getOrderHandler(InfDmpOrderSendVO order){
    	IOrderHandler orderHandler = null;
    	switch(order.getBusinessType()){
		case BusinessType.OXO_JIT:
			//oxo-jit
			orderHandler = oXOOrderHandler;
			break;
		case BusinessType.PEISONG:
			//唯品会正常单
			orderHandler = peisongOrderHandler;
			break;
		case BusinessType.OXO:
			//oxo直送
			orderHandler = oXOOrderHandler;
			break;
		case BusinessType.SHANGMENTUI:
			// 揽退
			orderHandler = shangmentuiOrderHandler;
			break;
		case BusinessType.EXPRESS:
			// 快递
			orderHandler = expressOrderHandler;
			break;			
		}
    	return orderHandler;
    }
    
}
