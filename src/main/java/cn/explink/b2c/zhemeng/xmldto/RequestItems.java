package cn.explink.b2c.zhemeng.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class RequestItems {
	private String itemno;
	private String province;
	private String city;
	private String area;
	private String address;
	
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
