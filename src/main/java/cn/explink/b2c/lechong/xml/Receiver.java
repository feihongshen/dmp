package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;

public class Receiver {
	private String RName;// 寄件方姓名
	private String RPhone;// 寄件方座机
	private String RMobile;// 寄件方手机
	private String RProvince;// 寄件方省份
	private String RCity;// 寄件方城市
	private String RAddress;// 寄件方地址
	private String Zip;// 收件方邮政编码

	@XmlElement(name = "RName")
	public String getRName() {
		return RName;
	}

	public void setRName(String rName) {
		RName = rName;
	}

	@XmlElement(name = "RPhone")
	public String getRPhone() {
		return RPhone;
	}

	public void setRPhone(String rPhone) {
		RPhone = rPhone;
	}

	@XmlElement(name = "RMobile")
	public String getRMobile() {
		return RMobile;
	}

	public void setRMobile(String rMobile) {
		RMobile = rMobile;
	}

	@XmlElement(name = "RProvince")
	public String getRProvince() {
		return RProvince;
	}

	public void setRProvince(String rProvince) {
		RProvince = rProvince;
	}

	@XmlElement(name = "RCity")
	public String getRCity() {
		return RCity;
	}

	public void setRCity(String rCity) {
		RCity = rCity;
	}

	@XmlElement(name = "RAddress")
	public String getRAddress() {
		return RAddress;
	}

	public void setRAddress(String rAddress) {
		RAddress = rAddress;
	}

	@XmlElement(name = "Zip")
	public String getZip() {
		return Zip;
	}

	public void setZip(String zip) {
		Zip = zip;
	}
}
