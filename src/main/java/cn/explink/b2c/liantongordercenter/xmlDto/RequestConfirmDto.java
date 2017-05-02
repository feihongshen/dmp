package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
public class RequestConfirmDto {
	private String head;
	private RequestConfirmBodyDto requestConfirmDto;
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
	public RequestConfirmBodyDto getRequestConfirmDto() {
		return this.requestConfirmDto;
	}

	public void setRequestConfirmDto(RequestConfirmBodyDto requestConfirmDto) {
		this.requestConfirmDto = requestConfirmDto;
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
