package cn.explink.b2c.gztlfeedback.feedbacksenddata;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMSD {
	@XmlElementWrapper(name = "Orders")
	@XmlElement(name = "Order")
	private List<SendOrder> orders;

	public List<SendOrder> getOrders() {
		return this.orders;
	}

	public void setOrders(List<SendOrder> orders) {
		this.orders = orders;
	}

}
