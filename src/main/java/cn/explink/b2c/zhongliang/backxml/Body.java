package cn.explink.b2c.zhongliang.backxml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Body {

	private List<Order> order;

	@XmlElement(name = "Order")
	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}
}
