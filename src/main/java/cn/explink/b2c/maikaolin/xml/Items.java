package cn.explink.b2c.maikaolin.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.maikaolin.xml.Item;
import cn.explink.pos.alipay.xml.item;

@XmlRootElement(name = "Items")
public class Items {
	private List<Item> Itemlist = null;

	@XmlElement(name = "Item")
	public List<Item> getItemlist() {
		return Itemlist;
	}

	public void setItemlist(List<Item> itemlist) {
		Itemlist = itemlist;
	}

}
