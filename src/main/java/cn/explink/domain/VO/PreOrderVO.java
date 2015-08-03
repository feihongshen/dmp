package cn.explink.domain.VO;

public class PreOrderVO {

	private String id;
	private String preOrderNum;
	private String creatTime;
	private String sendPerson;
	private String cellPhone;
	private String telePhone;
	private String takeAddress;
	private String reason;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPreOrderNum() {
		return preOrderNum;
	}
	public void setPreOrderNum(String preOrderNum) {
		this.preOrderNum = preOrderNum;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getSendPerson() {
		return sendPerson;
	}
	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getTakeAddress() {
		return takeAddress;
	}
	public void setTakeAddress(String takeAddress) {
		this.takeAddress = takeAddress;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
