package cn.explink.b2c.weisuda.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class PushOrders_Root {

	private List<PushOrders_Item> item;

	@XmlElement(name = "item")
	public List<PushOrders_Item> getItem() {
		return item;
	}

	public void setItem(List<PushOrders_Item> item) {
		this.item = item;
	}
}
