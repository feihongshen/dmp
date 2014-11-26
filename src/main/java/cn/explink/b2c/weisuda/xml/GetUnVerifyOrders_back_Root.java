package cn.explink.b2c.weisuda.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class GetUnVerifyOrders_back_Root {

	private List<GetUnVerifyOrders_back_Item> item;

	@XmlElement(name = "item")
	public List<GetUnVerifyOrders_back_Item> getItem() {
		return item;
	}

	public void setItem(List<GetUnVerifyOrders_back_Item> item) {
		this.item = item;
	}
}
