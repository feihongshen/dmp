package cn.explink.b2c.weisuda.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class UpdateUnVerifyOrders_Root {

	private List<UpdateUnVerifyOrders_back_Item> item;

	@XmlElement(name = "item")
	public List<UpdateUnVerifyOrders_back_Item> getItem() {
		return item;
	}

	public void setItem(List<UpdateUnVerifyOrders_back_Item> item) {
		this.item = item;
	}
}
