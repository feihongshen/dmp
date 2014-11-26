package cn.explink.b2c.wangjiu.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class OrderItems {

	private List<OrderItem> item;

	@XmlElement(name = "item")
	public List<OrderItem> getItem() {
		return item;
	}

	public void setItem(List<OrderItem> item) {
		this.item = item;
	}

}
