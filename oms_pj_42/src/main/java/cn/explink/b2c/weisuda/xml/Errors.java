package cn.explink.b2c.weisuda.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class Errors {
	private String code;
	private String msg;

	@XmlElement(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	@XmlElement(name = "msg")
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
