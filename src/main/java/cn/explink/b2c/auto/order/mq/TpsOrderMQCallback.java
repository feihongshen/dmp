package cn.explink.b2c.auto.order.mq;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
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
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

import com.vip.platform.middleware.vms.IPublisher;
import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.Message;
import com.vip.platform.middleware.vms.VMSClient;
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
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@SuppressWarnings("unused")
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
			JointEntity jointEntityByShipper = this.jiontDAO.getJointEntityByShipperNoForUse("\""+orderSend.getCustCode()+"\"");
			if(jointEntityByShipper == null){
				this.logger.info("tps订单下发接口，承运商对应的配置不存在,承运商号：{}", orderSend.getCustCode());
				feedbackException(msg, "tps订单下发接口，承运商对应的配置不存在", orderSend.getTransportNo());
				return;
			}
			vipshop = this.getVipShop(jointEntityByShipper.getJoint_num());
			int isOpenFlag = this.jointService.getStateForJoint(jointEntityByShipper.getJoint_num());
			if (isOpenFlag == 1) {
				if(jointEntityByShipper!=null){
					orderHandler = getOrderHandler(orderSend);
					if(orderHandler == null){
						feedbackException(msg, "传入business_type不正确", orderSend.getCustOrderNo());
					}
					orderHandler.dealWith(orderSend,vipshop);	
				}else{
					this.logger.info("没有对应的承运商接口或者该承运商编码对应的接口未开启,承运上编码为：【"+orderSend.getCustCode()+"】");
					throw new CwbException(orderSend.getCustOrderNo(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),"没有对应的承运商接口或者该承运商编码对应的接口未开启,承运上编码为：【"+orderSend.getCustCode()+"】");
				}
			}else{
		    	this.logger.info("未开启TPS订单下发接口[" + jointEntityByShipper.getJoint_num() + "]对接！");
		    	throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"未开启TPS订单下发接口[" + jointEntityByShipper.getJoint_num() + "]对接！");
		    }
					
		} catch (Exception ex) {
			ex.printStackTrace();
			this.logger.error("消费TPS订单下发数据时解析异常!", ex);
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
		long detailId = this.autoExceptionService.createAutoExceptionDetail(cwb, "", "TPS订单下发数据异常", AutoExceptionStatusEnum.xinjian.getValue(), msgid, 0, "");
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
		String routingKeyStr[] = consumerTemplate.getQueueName().split("_");
		String routingKey = "*";
		if(routingKeyStr.length>1){
			routingKey = routingKeyStr[routingKeyStr.length-1];
		}
		sub.append("<root>");
		sub.append("<system_name>ZDH_Order</system_name>");
		sub.append("<queue_name>" + consumerTemplate.getQueueName() + "</queue_name>");
		sub.append("<exchange_name>" + consumerTemplate.getExchangeName() + "</exchange_name>");
		sub.append("<routing_key>"+routingKey+"</routing_key>");
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
    
    
  //获取唯品会接口配置对象
    public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}
    
    public static void main(String args[]){
    	String str = "订单新下发";
    	VMSClient client = VMSClient.getDefault();//new VMSClient();//消耗大量的资源,通过VMSClient.getDefault()获取单例????????
        Message msg = Message.from(str);
        msg.addRoutingKey("30113");
        msg.qos().durable(true); // 非持久化的消息在宕机后消息会丢失。对于订单/运单类消息，必须设置为持久化。
        // msg.qos().priority(0); // 数字大的表示优先级高。 在同一个topic中，优先级高的消息先于优先级低的消息被消费。可选设置。
        // 推送到MQ
        client.options().setConfirmable(true).setWaitingTimeout(2000).setFailFastEnabled(false);
        IPublisher publisher = client.publish("channel.rabbitmq.tps.express.dmp", msg);// 要改回异常channel 
    }
}
