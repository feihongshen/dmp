package cn.explink.b2c.zjfeiyuan.requestdto;

import javax.xml.bind.annotation.XmlElement;

public class Headers {
	private String usercode;
	private String batchno;
	private String key;
	@XmlElement(name="usercode")
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	@XmlElement(name="batchno")
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	@XmlElement(name="key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
