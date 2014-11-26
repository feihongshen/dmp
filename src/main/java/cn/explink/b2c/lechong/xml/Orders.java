package cn.explink.b2c.lechong.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Orders {
	private List<Order> order;

	@XmlElement(name = "Order")
	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}
}
