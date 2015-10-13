package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Request_UpdateStatus {
	private String SendorderID = "";
	private String InfoType = "";
	private String OrderStatus = "";
	private String ChangeTime = "";
	private String MailNo = "";
	private String Remark = "";

	@XmlElement(name = "SendorderID")
	public String getSendorderID() {
		return SendorderID;
	}

	public void setSendorderID(String sendorderID) {
		SendorderID = sendorderID;
	}

	@XmlElement(name = "InfoType")
	public String getInfoType() {
		return InfoType;
	}

	public void setInfoType(String infoType) {
		InfoType = infoType;
	}

	@XmlElement(name = "OrderStatus")
	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	@XmlElement(name = "ChangeTime")
	public String getChangeTime() {
		return ChangeTime;
	}

	public void setChangeTime(String changeTime) {
		ChangeTime = changeTime;
	}

	@XmlElement(name = "MailNo")
	public String getMailNo() {
		return MailNo;
	}

	public void setMailNo(String mailNo) {
		MailNo = mailNo;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}
}
