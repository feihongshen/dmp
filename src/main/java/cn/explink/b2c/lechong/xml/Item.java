package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	private String ProductCode;// >商品编码
	private String ProductName;// 商品名称
	private String Number;// 数量
	private String SellPrice;// 单价

	@XmlElement(name = "ProductCode")
	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	@XmlElement(name = "ProductName")
	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	@XmlElement(name = "Number")
	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	@XmlElement(name = "SellPrice")
	public String getSellPrice() {
		return SellPrice;
	}

	public void setSellPrice(String sellPrice) {
		SellPrice = sellPrice;
	}
}
