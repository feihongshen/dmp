package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateInfo")
public class UpdateInfo {
	private String LogisticProviderID;
	private String DoID;
	private String MailNO;
	private String InfoType;
	private String Remark;

	@XmlElement(name = "LogisticProviderID")
	public String getLogisticProviderID() {
		return LogisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		LogisticProviderID = logisticProviderID;
	}

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

	@XmlElement(name = "InfoType")
	public String getInfoType() {
		return InfoType;
	}

	public void setInfoType(String infoType) {
		InfoType = infoType;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

}
