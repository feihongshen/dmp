package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Response_GoodDetail {
	private String ID;

	private String SendOrderID;
	private String GoodsID;
	private String GoodsName;
	private String QTY;
	private String UnitName;
	private String Price;
	private String Remark;

	@XmlElement(name = "ID")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	@XmlElement(name = "SendOrderID")
	public String getSendOrderID() {
		return SendOrderID;
	}
	
	public void setSendOrderID(String sendOrderID) {
		SendOrderID = sendOrderID;
	}

	@XmlElement(name = "GoodsID")
	public String getGoodsID() {
		return GoodsID;
	}

	public void setGoodsID(String goodsID) {
		GoodsID = goodsID;
	}

	@XmlElement(name = "GoodsName")
	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	@XmlElement(name = "QTY")
	public String getQTY() {
		return QTY;
	}

	public void setQTY(String qTY) {
		QTY = qTY;
	}

	@XmlElement(name = "UnitName")
	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	@XmlElement(name = "Price")
	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}
}
