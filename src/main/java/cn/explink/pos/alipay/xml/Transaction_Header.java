package cn.explink.pos.alipay.xml;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Header {

	private String transaction_id;
	private String requester;
	private String target;
	private String request_time;
	private String version;
	private ext_attributes ext_attributes;
	private String MAC;

	private String resp_code;
	private String resp_msg;
	private String resp_time;
	private String serial_no;

	@XmlElement(name = "resp_code")
	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	@XmlElement(name = "resp_msg")
	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	@XmlElement(name = "resp_time")
	public String getResp_time() {
		return resp_time;
	}

	public void setResp_time(String resp_time) {
		this.resp_time = resp_time;
	}

	@XmlElement(name = "serial_no")
	public String getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	@XmlElement(name = "MAC")
	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	@XmlElement(name = "ext_attributes")
	public ext_attributes getExt_attributes() {
		return ext_attributes;
	}

	public void setExt_attributes(ext_attributes ext_attributes) {
		this.ext_attributes = ext_attributes;
	}

	@XmlElement(name = "transaction_id")
	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	@XmlElement(name = "requester")
	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	@XmlElement(name = "target")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@XmlElement(name = "request_time")
	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	@XmlElement(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
