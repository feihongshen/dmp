package cn.explink.pos.tonglianpos.delivery.respDto;

import javax.xml.bind.annotation.XmlElement;


public class ErrorInfo {

	
	private String cwb; 
	private String msg;
	@XmlElement(name = "cwb")
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	@XmlElement(name = "msg")
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	} 
	
}
