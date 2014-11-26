package cn.explink.b2c.amazon.domain.manifestDetail;

import javax.xml.bind.annotation.XmlElement;

//收件人地址
public class ConsigneeAddress {
	private String name;// 姓名
	private String addressLine1;// 地址1
	private String addressLine2;// 地址1
	private String addressLine3;// 地址1
	private String district;// 区
	private String city;// 市
	private StateChoice stateChoice;// 省
	private String zip;// 右边
	private String countryCode;// 国籍
	private String countryName;// 国家
	private String contactPhone;// 手机
	private String contactEmail;// 邮箱
	private String contactEmailFull;// 邮箱
	private String amzShipAddressUsage;// -地址类型, R表示居民
	private String addressGUID;// 自提点id

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "addressLine1")
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@XmlElement(name = "addressLine2")
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@XmlElement(name = "addressLine3")
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@XmlElement(name = "district")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@XmlElement(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "stateChoice")
	public StateChoice getStateChoice() {
		return stateChoice;
	}

	public void setStateChoice(StateChoice stateChoice) {
		this.stateChoice = stateChoice;
	}

	@XmlElement(name = "zip")
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@XmlElement(name = "countryCode")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@XmlElement(name = "countryName")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@XmlElement(name = "contactPhone")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@XmlElement(name = "contactEmail")
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@XmlElement(name = "amzShipAddressUsage")
	public String getAmzShipAddressUsage() {
		return amzShipAddressUsage;
	}

	public void setAmzShipAddressUsage(String amzShipAddressUsage) {
		this.amzShipAddressUsage = amzShipAddressUsage;
	}

	@XmlElement(name = "amzShipAddressUsage")
	public String getContactEmailFull() {
		return contactEmailFull;
	}

	public void setContactEmailFull(String contactEmailFull) {
		this.contactEmailFull = contactEmailFull;
	}

	@XmlElement(name = "AddressGUID")
	public String getAddressGUID() {
		return addressGUID;
	}

	public void setAddressGUID(String addressGUID) {
		this.addressGUID = addressGUID;
	}

}
