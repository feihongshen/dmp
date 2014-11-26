package cn.explink.b2c.liantong.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class Receiver {

	@JsonProperty(value = "RevCompany")
	private String revCompany;
	@JsonProperty(value = "RevName")
	private String revName;
	@JsonProperty(value = "RevPostcode")
	private String revPostcode;
	@JsonProperty(value = "RevMobile")
	private String revMobile;
	@JsonProperty(value = "RevTel")
	private String revTel;
	@JsonProperty(value = "RevProvince")
	private String revProvince;

	public String getRevCompany() {
		return revCompany;
	}

	public void setRevCompany(String revCompany) {
		this.revCompany = revCompany;
	}

	public String getRevName() {
		return revName;
	}

	public void setRevName(String revName) {
		this.revName = revName;
	}

	public String getRevPostcode() {
		return revPostcode;
	}

	public void setRevPostcode(String revPostcode) {
		this.revPostcode = revPostcode;
	}

	public String getRevMobile() {
		return revMobile;
	}

	public void setRevMobile(String revMobile) {
		this.revMobile = revMobile;
	}

	public String getRevTel() {
		return revTel;
	}

	public void setRevTel(String revTel) {
		this.revTel = revTel;
	}

	public String getRevProvince() {
		return revProvince;
	}

	public void setRevProvince(String revProvince) {
		this.revProvince = revProvince;
	}

	public String getRevCity() {
		return revCity;
	}

	public void setRevCity(String revCity) {
		this.revCity = revCity;
	}

	public String getRevCounty() {
		return revCounty;
	}

	public void setRevCounty(String revCounty) {
		this.revCounty = revCounty;
	}

	public String getRevAddress() {
		return revAddress;
	}

	public void setRevAddress(String revAddress) {
		this.revAddress = revAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonProperty(value = "RevCity")
	private String revCity;
	@JsonProperty(value = "RevCounty")
	private String revCounty;
	@JsonProperty(value = "RevAddress")
	private String revAddress;
	@JsonProperty(value = "Remark")
	private String remark;
}
