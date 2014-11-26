package cn.explink.b2c.vipshop.address.domain.request;

import javax.xml.bind.annotation.XmlElement;

public class RequestHead {
	private String usercode;// 接口调用方编号
	private String batchno;// 批次号
	private String key;// 接口调用密文

	@XmlElement(name = "usercode")
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	@XmlElement(name = "batchno")
	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	@XmlElement(name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
