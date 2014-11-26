package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;

public class FddVO {
	private String deliveryWeekendFlag;// 客户可收货时间类别
	private String fddFlag;// 固定送达日期标识
	private String fddDate;// 固定送达日期
	private String deliveryTimeslot;// 客户可收货时间
	private String comments;// 备注

	@XmlElement(name = "deliveryWeekendFlag")
	public String getDeliveryWeekendFlag() {
		return deliveryWeekendFlag;
	}

	public void setDeliveryWeekendFlag(String deliveryWeekendFlag) {
		this.deliveryWeekendFlag = deliveryWeekendFlag;
	}

	@XmlElement(name = "fddFlag")
	public String getFddFlag() {
		return fddFlag;
	}

	public void setFddFlag(String fddFlag) {
		this.fddFlag = fddFlag;
	}

	@XmlElement(name = "deliveryWeekendFlag")
	public String getFddDate() {
		return fddDate;
	}

	public void setFddDate(String fddDate) {
		this.fddDate = fddDate;
	}

	@XmlElement(name = "deliveryTimeslot")
	public String getDeliveryTimeslot() {
		return deliveryTimeslot;
	}

	public void setDeliveryTimeslot(String deliveryTimeslot) {
		this.deliveryTimeslot = deliveryTimeslot;
	}

	@XmlElement(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
