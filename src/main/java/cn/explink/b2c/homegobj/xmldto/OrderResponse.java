package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Jiayou")
public class OrderResponse {

	private Header header;

	@XmlElement(name = "header")
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	private ResponseBody responseBody;

	@XmlElement(name = "body")
	public ResponseBody getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(ResponseBody responseBody) {
		this.responseBody = responseBody;
	}

}
