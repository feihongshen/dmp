package cn.explink.b2c.weisuda.xml;

import javax.xml.bind.annotation.XmlElement;

public class UpdateUnVerifyOrders_back_Item {
	private String order_id;

	@XmlElement(name = "order_id")
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

}
