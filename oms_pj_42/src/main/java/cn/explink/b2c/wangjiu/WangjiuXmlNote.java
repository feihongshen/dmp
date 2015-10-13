package cn.explink.b2c.wangjiu;

import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 *
 */
public class WangjiuXmlNote {

	private String mailNo;
	private String infoContent;
	private String name;
	private Timestamp acceptTime;

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}

}
