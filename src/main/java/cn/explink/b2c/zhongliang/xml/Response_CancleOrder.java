package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Response")
public class Response_CancleOrder {
	private String head;
	private String service;
	private String lang;
	private Response_CancleOrder_Body response_CancleOrder_body;
	@XmlElement(name = "Head")
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlAttribute(name = "service")
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@XmlAttribute(name = "lang")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	public Response_CancleOrder_Body getResponse_CancleOrder_body() {
		return response_CancleOrder_body;
	}
	@XmlElement(name = "Body")
	public void setResponse_CancleOrder_body(
			Response_CancleOrder_Body response_CancleOrder_body) {
		this.response_CancleOrder_body = response_CancleOrder_body;
	}
}
