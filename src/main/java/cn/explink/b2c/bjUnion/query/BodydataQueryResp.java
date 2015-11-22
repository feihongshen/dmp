package cn.explink.b2c.bjUnion.query;

import javax.xml.bind.annotation.XmlElement;

public class BodydataQueryResp{
	private String amt;
	private String consignee;
	private String consignee_contact;
	private String consignee_address;
	private String status;
	private String desc;
	private String merchant_code;
	private String account_key;
	@XmlElement(name = "amt")
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	@XmlElement(name = "consignee")
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	@XmlElement(name = "consignee_contact")
	public String getConsignee_contact() {
		return consignee_contact;
	}
	public void setConsignee_contact(String consignee_contact) {
		this.consignee_contact = consignee_contact;
	}
	@XmlElement(name = "consignee_address")
	public String getConsignee_address() {
		return consignee_address;
	}
	public void setConsignee_address(String consignee_address) {
		this.consignee_address = consignee_address;
	}
	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@XmlElement(name = "desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@XmlElement(name = "merchant_code")
	public String getMerchant_code() {
		return merchant_code;
	}
	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}
	@XmlElement(name = "account_key")
	public String getAccount_key() {
		return account_key;
	}
	public void setAccount_key(String account_key) {
		this.account_key = account_key;
	}
	
}
