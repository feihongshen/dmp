package cn.explink.b2c.lechong.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Items {

	private List<Item> item;

	@XmlElement(name = "Item")
	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}
}
