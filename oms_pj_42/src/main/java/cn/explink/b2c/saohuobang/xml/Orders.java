package cn.explink.b2c.saohuobang.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
public class Orders {
	List<Order> listorder = null;

	@XmlElement(name = "order")
	public List<Order> getListorder() {
		return listorder;
	}

	public void setListorder(List<Order> listorder) {
		this.listorder = listorder;
	}

}
