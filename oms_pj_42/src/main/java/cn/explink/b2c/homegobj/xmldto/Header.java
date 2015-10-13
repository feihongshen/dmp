package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class Header {
	private String function_id;
	private String version;
	private String app_id;
	private String charset;
	private String sign_type;
	private String signed;
	private String data_type;
	private String request_time;

	@XmlElement(name = "function_id")
	public String getFunction_id() {
		return function_id;
	}

	public void setFunction_id(String function_id) {
		this.function_id = function_id;
	}

	@XmlElement(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "app_id")
	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	@XmlElement(name = "charset")
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@XmlElement(name = "sign_type")
	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	@XmlElement(name = "signed")
	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	@XmlElement(name = "data_type")
	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	@XmlElement(name = "request_time")
	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

}
