package cn.explink.b2c.wangjiu.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class OrderItem {

	private String itemName;
	private long number;
	private String itemValue;

	@XmlElement(name = "itemName")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@XmlElement(name = "number")
	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	@XmlElement(name = "itemValue")
	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}
