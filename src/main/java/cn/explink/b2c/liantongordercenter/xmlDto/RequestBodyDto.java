package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body")
public class RequestBodyDto {

	private OrderDto order;

	@XmlElement(name = "Order")
	public OrderDto getOrder() {
		return this.order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

}
