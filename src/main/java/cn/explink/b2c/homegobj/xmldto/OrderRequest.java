package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Jiayou")
public class OrderRequest {
	private Header requestHeader;
	private RequestBody requestBody;

	@XmlElement(name = "header")
	public Header getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Header requestHeader) {
		this.requestHeader = requestHeader;
	}

	@XmlElement(name = "body")
	public RequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

}
