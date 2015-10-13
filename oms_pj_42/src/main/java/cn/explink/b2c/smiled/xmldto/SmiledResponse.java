package cn.explink.b2c.smiled.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class SmiledResponse {

	private String workCode; // 运单号
	private String is_success; // 结果 T F
	private String error; //

	@XmlElement(name = "WorkCode")
	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@XmlElement(name = "IS_SUCCESS")
	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	@XmlElement(name = "Error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
