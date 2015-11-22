package cn.explink.b2c.bjUnion;

import javax.xml.bind.annotation.XmlElement;

public class HeaderdataReq {
	private String version;
	private String transaction_id;
	private String employno;
	private String termid;
	private String request_time;
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
	@XmlElement(name = "request_time")
	public String getRequest_time() {
		return request_time;
	}
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	@XmlElement(name = "mac")
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
