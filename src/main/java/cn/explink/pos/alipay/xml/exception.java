package cn.explink.pos.alipay.xml;

import javax.xml.bind.annotation.XmlElement;

public class exception {
	private String code;
	private String msg;

	@XmlElement(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name = "msg")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
