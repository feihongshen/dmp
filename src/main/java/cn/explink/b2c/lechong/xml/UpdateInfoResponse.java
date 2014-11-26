package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateInfoResponse")
public class UpdateInfoResponse {
	private String DoID;
	private String MailNO;
	private String flag;
	private String desc;

	@XmlElement(name = "DoID")
	public String getDoID() {
		return DoID;
	}

	public void setDoID(String doID) {
		DoID = doID;
	}

	@XmlElement(name = "MailNO")
	public String getMailNO() {
		return MailNO;
	}

	public void setMailNO(String mailNO) {
		MailNO = mailNO;
	}

	@XmlElement(name = "flag")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@XmlElement(name = "desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
