package cn.explink.b2c.homegobj.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Orders {

	private List<OrderDto> orderDto;

	@XmlElement(name = "order")
	public List<OrderDto> getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(List<OrderDto> orderDto) {
		this.orderDto = orderDto;
	}

}
