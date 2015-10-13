package cn.explink.b2c.saohuobang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Order {

	private String mailNo;

	@XmlElement(name = "mailNo")
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

}
