package cn.explink.pos.chinaums.xml;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Header {

	private String version;// 版本号
	private String transtype;// 请求类型
	private String employno;// 小件员账号
	private String termid;// 终端号
	private String request_time;// 请求时间
	private String mac;// 密文

	private String response_time;// 报文应答时间
	private String response_code;// 应答编号
	private String response_msg;// 应答消息

	@XmlElement(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "transtype")
	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
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

}
