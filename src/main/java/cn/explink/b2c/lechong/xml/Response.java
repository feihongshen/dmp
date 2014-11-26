package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {
	private OrdersResponse ordersResponses;

	@XmlElement(name = "OrdersResponse")
	public OrdersResponse getOrdersResponses() {
		return ordersResponses;
	}

	public void setOrdersResponses(OrdersResponse ordersResponses) {
		this.ordersResponses = ordersResponses;
	}
}
