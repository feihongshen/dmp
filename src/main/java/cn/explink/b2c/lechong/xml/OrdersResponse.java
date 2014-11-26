package cn.explink.b2c.lechong.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class OrdersResponse {
	private List<OrderResponse> orderResponse;

	@XmlElement(name = "OrderResponse")
	public List<OrderResponse> getOrderResponse() {
		return orderResponse;
	}

	public void setOrderResponse(List<OrderResponse> orderResponse) {
		this.orderResponse = orderResponse;
	}

}
