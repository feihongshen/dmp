package cn.explink.b2c.wanxiang.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class WxResponse {

	private String custCode;
	private String state;
	private Wxlog log;

	@XmlElement(name = "custCode")
	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	@XmlElement(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@XmlElement(name = "log")
	public Wxlog getLog() {
		return log;
	}

	public void setLog(Wxlog log) {
		this.log = log;
	}
}
