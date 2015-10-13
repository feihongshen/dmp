package cn.explink.b2c.wanxiang.bean;

import javax.xml.bind.annotation.XmlElement;

public class Wxlog {

	private String bill_no;
	private String error_code;
	private String error_msg;

	@XmlElement(name = "bill_no")
	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	@XmlElement(name = "error_code")
	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	@XmlElement(name = "error_msg")
	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

}
