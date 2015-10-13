package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Jiayou")
public class OrderStatusRequest {
	private Header requestHeader;
	private OrderStatusBody requestBody;

	@XmlElement(name = "header")
	public Header getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Header requestHeader) {
		this.requestHeader = requestHeader;
	}

	@XmlElement(name = "body")
	public OrderStatusBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(OrderStatusBody requestBody) {
		this.requestBody = requestBody;
	}

}
