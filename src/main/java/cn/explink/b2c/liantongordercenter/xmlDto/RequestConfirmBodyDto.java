package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body")
public class RequestConfirmBodyDto {

	private OrderConfirm orderConfirm;

	@XmlElement(name = "OrderConfirm")
	public OrderConfirm getOrderConfirm() {
		return this.orderConfirm;
	}

	public void setOrderConfirm(OrderConfirm orderConfirm) {
		this.orderConfirm = orderConfirm;
	}

}
