package cn.explink.b2c.vipshop.addresspre.request;

import javax.xml.bind.annotation.XmlElement;

public class RequestHead {
	private String usercode;// 接口调用方编号
	private String batchno;// 批次号

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

}
