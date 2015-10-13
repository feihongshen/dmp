package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateInfo")
public class UpdateInfo {
	private String LogisticProvider;
	private String LogisticProviderID;
	private String DoID;
	private String MailNO;
	private String Status;
	private String AcceptTime;
	private String ReceiveEmployee;
	private String City;
	private String Remark;
	private String STATUS_REASON;
	private String MD5Key;

	@XmlElement(name = "LogisticProvider")
	public String getLogisticProvider() {
		return LogisticProvider;
	}

	public void setLogisticProvider(String logisticProvider) {
		LogisticProvider = logisticProvider;
	}

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

	@XmlElement(name = "Status")
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@XmlElement(name = "AcceptTime")
	public String getAcceptTime() {
		return AcceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		AcceptTime = acceptTime;
	}

	@XmlElement(name = "ReceiveEmployee")
	public String getReceiveEmployee() {
		return ReceiveEmployee;
	}

	public void setReceiveEmployee(String receiveEmployee) {
		ReceiveEmployee = receiveEmployee;
	}

	@XmlElement(name = "City")
	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	@XmlElement(name = "STATUS_REASON")
	public String getSTATUS_REASON() {
		return STATUS_REASON;
	}

	public void setSTATUS_REASON(String sTATUS_REASON) {
		STATUS_REASON = sTATUS_REASON;
	}

	@XmlElement(name = "MD5Key")
	public String getMD5Key() {
		return MD5Key;
	}

	public void setMD5Key(String mD5Key) {
		MD5Key = mD5Key;
	}

}
