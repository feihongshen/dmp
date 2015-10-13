package cn.explink.b2c.wangjiu.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class QueryOrder {

	private String mailNo;// 运单号

	@XmlElement(name = "mailNo")
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
}
