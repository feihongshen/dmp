package cn.explink.b2c.zjfeiyuan.responsedto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="response")
public class ResponseData {
	private Headers head;
	private ItemsBody items;
	
	@XmlElement(name="head")
	public Headers getHead() {
		return head;
	}
	public void setHead(Headers head) {
		this.head = head;
	}
	
	@XmlElement(name="items")
	public ItemsBody getItems() {
		return items;
	}
	public void setItems(ItemsBody items) {
		this.items = items;
	}
}
