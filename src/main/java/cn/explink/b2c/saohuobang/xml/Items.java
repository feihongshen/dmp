package cn.explink.b2c.saohuobang.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
public class Items {
	List<Item> listitem = null;

	public void setListitem(List<Item> listitem) {
		this.listitem = listitem;
	}

	@XmlElement(name = "item")
	public List<Item> getListitem() {
		return listitem;
	}

}