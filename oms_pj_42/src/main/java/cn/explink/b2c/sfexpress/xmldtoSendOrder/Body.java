package cn.explink.b2c.sfexpress.xmldtoSendOrder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下单至顺丰返回的bean
 * 
 * @author Administrator
 *
 */

public class Body {

	private OrderResponse orderResponse;// 头信息

	@XmlElement(name = "OrderResponse")
	public OrderResponse getOrderResponse() {
		return orderResponse;
	}

	public void setOrderResponse(OrderResponse orderResponse) {
		this.orderResponse = orderResponse;
	}

	private String filter_result;

	@XmlElement(name = "filter_result")
	public String getFilter_result() {
		return filter_result;
	}

	public void setFilter_result(String filter_result) {
		this.filter_result = filter_result;
	}

}
