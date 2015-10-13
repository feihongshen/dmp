package cn.explink.b2c.weisuda.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class UpdateOrders_root {
	private List<String> order_id;

	@XmlElement(name = "order_id")
	public List<String> getOrder_id() {
		return order_id;
	}

	public void setOrder_id(List<String> order_id) {
		this.order_id = order_id;
	}
}
