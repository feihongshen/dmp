package cn.explink.b2c.zhongliang.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Response_Body {

	private List<Response_Order> order;

	@XmlElement(name = "Order")
	public List<Response_Order> getOrder() {
		return order;
	}

	public void setOrder(List<Response_Order> order) {
		this.order = order;
	}
}
