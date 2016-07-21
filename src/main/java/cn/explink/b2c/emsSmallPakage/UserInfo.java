package cn.explink.b2c.emsSmallPakage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * 用户信息
 * add by zhouhuan 
 * add time:2016-07-20
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfo {
	@XmlElement(name = "name")
	private String name;//用户名称
	@XmlElement(name = "postCode")
	private String postCode;//用户邮政编码
	@XmlElement(name = "phone")
	private String phone;//用户手机
	private String mobile;//用户电话
	private String prov;//用户省份
	private String city;//用户城市
	private String address;//用户地址
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
}
