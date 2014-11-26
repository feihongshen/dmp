package cn.explink.b2c.vipshop.addresspre.request;

import javax.xml.bind.annotation.XmlElement;

public class RequestItem {
	private String itemno;// 序号
	private String address;// 地址

	@XmlElement(name = "itemno")
	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno;
	}

	@XmlElement(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
