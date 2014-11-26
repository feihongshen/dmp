package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;

public class TruckInfo {
	private String truckNumber;// 卡车牌号
	private String truckDesc;// 卡车信息
	private String driverName;// 司机姓名
	private String driverPhoneNumber;// 司机手机
	private String comments;// 备注

	@XmlElement(name = "truckNumber")
	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	@XmlElement(name = "truckDesc")
	public String getTruckDesc() {
		return truckDesc;
	}

	public void setTruckDesc(String truckDesc) {
		this.truckDesc = truckDesc;
	}

	@XmlElement(name = "driverName")
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@XmlElement(name = "driverPhoneNumber")
	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
	}

	@XmlElement(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
