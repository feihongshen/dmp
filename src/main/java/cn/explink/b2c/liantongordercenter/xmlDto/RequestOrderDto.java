package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
public class RequestOrderDto {

	private String head;

	private RequestBodyDto bodyDto;

	private String service;
	private String lang;

	@XmlElement(name = "Head")
	public String getHead() {
		return this.head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlElement(name = "Body")
	public RequestBodyDto getBodyDto() {
		return this.bodyDto;
	}

	public void setBodyDto(RequestBodyDto bodyDto) {
		this.bodyDto = bodyDto;
	}

	@XmlAttribute(name = "service")
	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@XmlAttribute(name = "long")
	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
