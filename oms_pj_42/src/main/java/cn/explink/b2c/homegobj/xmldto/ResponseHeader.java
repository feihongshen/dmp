package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

/**
 * 返回的报文头信息
 * 
 * @author Administrator
 *
 */
public class ResponseHeader {
	private String function_id;
	private String app_id;
	private String signed;
	private String resp_time;
	private String resp_code;
	private String resp_msg;

	@XmlElement(name = "resp_time")
	public String getResp_time() {
		return resp_time;
	}

	public void setResp_time(String resp_time) {
		this.resp_time = resp_time;
	}

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

	@XmlElement(name = "function_id")
	public String getFunction_id() {
		return function_id;
	}

	public void setFunction_id(String function_id) {
		this.function_id = function_id;
	}

	@XmlElement(name = "app_id")
	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	@XmlElement(name = "signed")
	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

}
