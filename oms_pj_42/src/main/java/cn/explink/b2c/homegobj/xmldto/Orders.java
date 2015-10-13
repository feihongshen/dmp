package cn.explink.b2c.homegobj.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Orders {

	private List<Order> orderDto;

	@XmlElement(name = "order")
	public List<Order> getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(List<Order> orderDto) {
		this.orderDto = orderDto;
	}

}
