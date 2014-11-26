package cn.explink.b2c.amazon.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transmission")
public class Transmission {
	private Message message;// 消息信息

	@XmlElement(name = "message")
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
