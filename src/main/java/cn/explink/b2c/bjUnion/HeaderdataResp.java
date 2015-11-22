package cn.explink.b2c.bjUnion;

import javax.xml.bind.annotation.XmlElement;

public class HeaderdataResp {
	private String version;
	private String transaction_id;
	private String employno;
	private String termid;
	private String response_time;
	private String response_code;
	private String response_msg;
	private String mac;
	@XmlElement(name = "version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@XmlElement(name = "transaction_id")
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	@XmlElement(name = "employno")
	public String getEmployno() {
		return employno;
	}
	public void setEmployno(String employno) {
		this.employno = employno;
	}
	@XmlElement(name = "termid")
	public String getTermid() {
		return termid;
	}
	public void setTermid(String termid) {
		this.termid = termid;
	}
	@XmlElement(name = "response_time")
	public String getResponse_time() {
		return response_time;
	}
	public void setResponse_time(String response_time) {
		this.response_time = response_time;
	}
	@XmlElement(name = "response_code")
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	@XmlElement(name = "response_msg")
	public String getResponse_msg() {
		return response_msg;
	}
	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}
	@XmlElement(name = "mac")
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
}
