package cn.explink.b2c.zjfeiyuan.responsedto;

import javax.xml.bind.annotation.XmlElement;

public class Headers {
	private String msg;
	
	@XmlElement(name="msg")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
