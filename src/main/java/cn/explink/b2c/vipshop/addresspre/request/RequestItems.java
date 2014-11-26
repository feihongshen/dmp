package cn.explink.b2c.vipshop.addresspre.request;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class RequestItems {
	private List<RequestItem> requestItem;

	@XmlElement(name = "item")
	public List<RequestItem> getItem() {
		return requestItem;
	}

	public void setItem(List<RequestItem> requestItem) {
		this.requestItem = requestItem;
	}

}
