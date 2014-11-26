package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;

public class Sender {
	private String SName;// 寄件方姓名
	private String SPhone;// 寄件方座机
	private String SMobile;// 寄件方手机
	private String SProvince;// 寄件方省份
	private String SCity;// 寄件方城市
	private String SAddress;// 寄件方地址

	@XmlElement(name = "SName")
	public String getSName() {
		return SName;
	}

	public void setSName(String sName) {
		SName = sName;
	}

	@XmlElement(name = "SPhone")
	public String getSPhone() {
		return SPhone;
	}

	public void setSPhone(String sPhone) {
		SPhone = sPhone;
	}

	@XmlElement(name = "SMobile")
	public String getSMobile() {
		return SMobile;
	}

	public void setSMobile(String sMobile) {
		SMobile = sMobile;
	}

	@XmlElement(name = "SProvince")
	public String getSProvince() {
		return SProvince;
	}

	public void setSProvince(String sProvince) {
		SProvince = sProvince;
	}

	@XmlElement(name = "SCity")
	public String getSCity() {
		return SCity;
	}

	public void setSCity(String sCity) {
		SCity = sCity;
	}

	@XmlElement(name = "SAddress")
	public String getSAddress() {
		return SAddress;
	}

	public void setSAddress(String sAddress) {
		SAddress = sAddress;
	}

}
