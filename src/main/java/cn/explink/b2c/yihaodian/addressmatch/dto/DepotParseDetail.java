package cn.explink.b2c.yihaodian.addressmatch.dto;

import javax.xml.bind.annotation.XmlElement;

public class DepotParseDetail {

	private String city;
	private String consigneeAddress;
	private String district;
	private String doCode;
	private String province;
	private String reserveField1;
	private String reserveField2;
	private String reserveField3;
	private String reserveField4;
	private String reserveField5;
	private String reserveField6;

	@XmlElement(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "consigneeAddress")
	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	@XmlElement(name = "district")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@XmlElement(name = "doCode")
	public String getDoCode() {
		return doCode;
	}

	public void setDoCode(String doCode) {
		this.doCode = doCode;
	}

	@XmlElement(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@XmlElement(name = "reserveField1")
	public String getReserveField1() {
		return reserveField1;
	}

	public void setReserveField1(String reserveField1) {
		this.reserveField1 = reserveField1;
	}

	@XmlElement(name = "reserveField2")
	public String getReserveField2() {
		return reserveField2;
	}

	public void setReserveField2(String reserveField2) {
		this.reserveField2 = reserveField2;
	}

	@XmlElement(name = "reserveField3")
	public String getReserveField3() {
		return reserveField3;
	}

	public void setReserveField3(String reserveField3) {
		this.reserveField3 = reserveField3;
	}

	@XmlElement(name = "reserveField4")
	public String getReserveField4() {
		return reserveField4;
	}

	public void setReserveField4(String reserveField4) {
		this.reserveField4 = reserveField4;
	}

	@XmlElement(name = "reserveField5")
	public String getReserveField5() {
		return reserveField5;
	}

	public void setReserveField5(String reserveField5) {
		this.reserveField5 = reserveField5;
	}

	@XmlElement(name = "reserveField6")
	public String getReserveField6() {
		return reserveField6;
	}

	public void setReserveField6(String reserveField6) {
		this.reserveField6 = reserveField6;
	}

}
