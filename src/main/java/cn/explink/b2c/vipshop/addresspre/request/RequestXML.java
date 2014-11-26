package cn.explink.b2c.vipshop.addresspre.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class RequestXML {
	private RequestHead requestHead;
	private RequestItems requestItems;

	@XmlElement(name = "head")
	public RequestHead getHead() {
		return requestHead;
	}

	public void setHead(RequestHead requestHead) {
		this.requestHead = requestHead;
	}

	@XmlElement(name = "items")
	public RequestItems getItems() {
		return requestItems;
	}

	public void setItems(RequestItems requestItems) {
		this.requestItems = requestItems;
	}

}
