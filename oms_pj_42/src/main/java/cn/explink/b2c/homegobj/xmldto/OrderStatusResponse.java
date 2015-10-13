package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Jiayou")
public class OrderStatusResponse {
	private ResponseHeader responseHeader;
	private ResponseOrderBody responseOrderBody;

	@XmlElement(name = "body")
	public ResponseOrderBody getResponseOrderBody() {
		return responseOrderBody;
	}

	public void setResponseOrderBody(ResponseOrderBody responseOrderBody) {
		this.responseOrderBody = responseOrderBody;
	}

	@XmlElement(name = "header")
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

}
