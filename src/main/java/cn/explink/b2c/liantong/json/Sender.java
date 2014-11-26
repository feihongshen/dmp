package cn.explink.b2c.liantong.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class Sender {
	@JsonProperty(value = "SedCompany")
	private String sedCompany;
	@JsonProperty(value = "SedName")
	private String sedName;
	@JsonProperty(value = "SedPostcode")
	private String sedPostcode;
	@JsonProperty(value = "SedMobile")
	private String sedMobile;
	@JsonProperty(value = "SedTel")
	private String sedTel;
	@JsonProperty(value = "SedProvince")
	private String sedProvince;

	public String getSedCompany() {
		return sedCompany;
	}

	public void setSedCompany(String sedCompany) {
		this.sedCompany = sedCompany;
	}

	public String getSedName() {
		return sedName;
	}

	public void setSedName(String sedName) {
		this.sedName = sedName;
	}

	public String getSedPostcode() {
		return sedPostcode;
	}

	public void setSedPostcode(String sedPostcode) {
		this.sedPostcode = sedPostcode;
	}

	public String getSedMobile() {
		return sedMobile;
	}

	public void setSedMobile(String sedMobile) {
		this.sedMobile = sedMobile;
	}

	public String getSedTel() {
		return sedTel;
	}

	public void setSedTel(String sedTel) {
		this.sedTel = sedTel;
	}

	public String getSedProvince() {
		return sedProvince;
	}

	public void setSedProvince(String sedProvince) {
		this.sedProvince = sedProvince;
	}

	public String getSedCity() {
		return sedCity;
	}

	public void setSedCity(String sedCity) {
		this.sedCity = sedCity;
	}

	public String getSedCounty() {
		return sedCounty;
	}

	public void setSedCounty(String sedCounty) {
		this.sedCounty = sedCounty;
	}

	public String getSedAddress() {
		return sedAddress;
	}

	public void setSedAddress(String sedAddress) {
		this.sedAddress = sedAddress;
	}

	@JsonProperty(value = "SedCity")
	private String sedCity;
	@JsonProperty(value = "SedCounty")
	private String sedCounty;
	@JsonProperty(value = "SedAddress")
	private String sedAddress;
}
