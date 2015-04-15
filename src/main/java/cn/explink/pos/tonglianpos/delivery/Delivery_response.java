package cn.explink.pos.tonglianpos.delivery;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.pos.tonglianpos.delivery.respDto.ErrorInfo;

@XmlRootElement(name = "response")
public class Delivery_response {
	
	private String result_code;
	private String result_msg;
	private List<ErrorInfo> errorList;
	@XmlElement(name = "result_code")
	public String getResult_code() {
		return result_code;
	}
	public List<ErrorInfo> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<ErrorInfo> errorList) {
		this.errorList = errorList;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	@XmlElement(name = "result_msg")
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	
}
