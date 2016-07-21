package cn.explink.b2c.emsSmallPakage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * 商品信息
 * add by zhouhuan 
 * add time:2016-07-20
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	@XmlElement(name = "itemName")
	private String itemName;//商品名称
	@XmlElement(name = "number")
	private int number;//商品数量
	@XmlElement(name = "itemValue")
	private long itemValue;//商品价值
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public long getItemValue() {
		return itemValue;
	}
	public void setItemValue(long itemValue) {
		this.itemValue = itemValue;
	}
	
}
