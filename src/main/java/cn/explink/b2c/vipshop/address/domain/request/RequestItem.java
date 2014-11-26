package cn.explink.b2c.vipshop.address.domain.request;

import javax.xml.bind.annotation.XmlElement;

public class RequestItem {
	private String itemno;// 序号
	private String province;// 省
	private String city;// 市
	private String area;// 区、县
	private String address;// 地址

	@XmlElement(name = "itemno")
	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno;
	}

	@XmlElement(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@XmlElement(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@XmlElement(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
