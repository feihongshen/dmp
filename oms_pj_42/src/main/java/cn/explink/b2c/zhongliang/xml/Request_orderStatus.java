package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
public class Request_orderStatus {
	private String head = "";
	private String service = "";
	private String lang = "";
	private Request_Body response_body = new Request_Body();

	@XmlElement(name = "Head")
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlElement(name = "Body")
	public Request_Body getResponse_body() {
		return response_body;
	}

	public void setResponse_body(Request_Body response_body) {
		this.response_body = response_body;
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
}
