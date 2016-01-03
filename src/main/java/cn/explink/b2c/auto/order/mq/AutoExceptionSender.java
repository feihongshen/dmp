package cn.explink.b2c.auto.order.mq;

import com.vip.platform.middleware.vms.IPublisher;
import com.vip.platform.middleware.vms.Message;
import com.vip.platform.middleware.vms.VMSClient;

public class AutoExceptionSender {
	private String channel;

	public void send(String content){
		VMSClient client = VMSClient.getDefault();//new VMSClient();//消耗大量的资源,通过VMSClient.getDefault()获取单例????????
        Message msg = Message.from(content);
        msg.addRoutingKey("*");
        msg.qos().durable(true); // 非持久化的消息在宕机后消息会丢失。对于订单/运单类消息，必须设置为持久化。
        // msg.qos().priority(0); // 数字大的表示优先级高。 在同一个topic中，优先级高的消息先于优先级低的消息被消费。可选设置。
        // 推送到MQ
        client.options().setConfirmable(true).setWaitingTimeout(2000).setFailFastEnabled(false);
        IPublisher publisher = client.publish(this.channel, msg);// 要改回异常channel 

	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
