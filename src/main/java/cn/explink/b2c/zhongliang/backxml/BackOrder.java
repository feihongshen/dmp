package cn.explink.b2c.zhongliang.backxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class BackOrder {

	private String head;
	private String service;
	private String lang;
	private Body body;

	@XmlElement(name = "Head")
	public String getHead() {
		return this.head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlElement(name = "Body")
	public Body getBody() {
		return this.body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@XmlAttribute(name = "service")
	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@XmlAttribute(name = "lang")
	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
