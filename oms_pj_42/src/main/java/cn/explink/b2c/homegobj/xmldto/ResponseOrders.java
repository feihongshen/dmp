package cn.explink.b2c.homegobj.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ResponseOrders {

	private List<ResponseOrder> orderDto;

	@XmlElement(name = "order")
	public List<ResponseOrder> getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(List<ResponseOrder> orderDto) {
		this.orderDto = orderDto;
	}

}
