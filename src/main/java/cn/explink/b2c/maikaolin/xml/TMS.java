package cn.explink.b2c.maikaolin.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tms")
public class TMS {
	private Request_Header request_header;
	private Request_Body request_body;

	@XmlElement(name = "request_header")
	public Request_Header getRequest_header() {
		return request_header;
	}

	public void setRequest_header(Request_Header request_header) {
		this.request_header = request_header;
	}

	@XmlElement(name = "request_body")
	public Request_Body getRequest_body() {
		return request_body;
	}

	public void setRequest_body(Request_Body request_body) {
		this.request_body = request_body;
	}

}
