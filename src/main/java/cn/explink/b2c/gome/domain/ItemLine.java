package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;

public class ItemLine {
	private String boxId;// 包装箱编号
	private String boxCode;// 包装箱类型编码
	private String boxBarcode;// 箱条码
	private String grossWeight;// 毛重
	private String volumeWeight;// 体积重
	private String length;// 长
	private String width;// 宽
	private String height;// 高
	private String orderNumber;// 订单号
	private String comments;// 备注

	@XmlElement(name = "boxId")
	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	@XmlElement(name = "boxCode")
	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	@XmlElement(name = "boxBarcode")
	public String getBoxBarcode() {
		return boxBarcode;
	}

	public void setBoxBarcode(String boxBarcode) {
		this.boxBarcode = boxBarcode;
	}

	@XmlElement(name = "grossWeight")
	public String getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	@XmlElement(name = "volumeWeight")
	public String getVolumeWeight() {
		return volumeWeight;
	}

	public void setVolumeWeight(String volumeWeight) {
		this.volumeWeight = volumeWeight;
	}

	@XmlElement(name = "length")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@XmlElement(name = "width")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@XmlElement(name = "height")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@XmlElement(name = "orderNumber")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@XmlElement(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
