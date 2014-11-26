package cn.explink.b2c.smile;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class Good {

	private String goodsName; // 货物名称

	private BigDecimal goodsValue; // 货物金额

	private String goodsBarCode; // 店内码

	private String listType; // 0换进1换出

	private String iSInvoice; // 是否有发票0表示没有发票，1为需要发票

	@XmlElement(name = "GoodsName")
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@XmlElement(name = "GoodsValue")
	public BigDecimal getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(BigDecimal goodsValue) {
		this.goodsValue = goodsValue;
	}

	@XmlElement(name = "GoodsBarCode")
	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	@XmlElement(name = "ListType")
	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	@XmlElement(name = "ISInvoice")
	public String getiSInvoice() {
		return iSInvoice;
	}

	public void setiSInvoice(String iSInvoice) {
		this.iSInvoice = iSInvoice;
	}

}
