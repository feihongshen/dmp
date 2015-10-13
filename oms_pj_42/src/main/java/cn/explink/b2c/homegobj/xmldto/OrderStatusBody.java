package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class OrderStatusBody {

	private Orders orders;

	@XmlElement(name = "orders")
	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

}
