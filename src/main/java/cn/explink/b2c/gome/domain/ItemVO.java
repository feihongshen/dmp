package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;

public class ItemVO {

	private Item item;

	@XmlElement(name = "item")
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
