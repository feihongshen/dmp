package cn.explink.b2c.zjfeiyuan.responsedto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ItemsBody {
	private List<Item> items;
	
	@XmlElement(name="item")
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
