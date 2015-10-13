package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class ResponseOrderBody {

	private ResponseOrders orders;

	@XmlElement(name = "orders")
	public ResponseOrders getOrders() {
		return orders;
	}

	public void setOrders(ResponseOrders orders) {
		this.orders = orders;
	}

}
