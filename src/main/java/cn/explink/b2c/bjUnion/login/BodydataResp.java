package cn.explink.b2c.bjUnion.login;

import javax.xml.bind.annotation.XmlElement;

public class BodydataResp {
	private String delivery_name;
	private String delivery_dept_no;
	private String delivery_dept;
	private String delivery_zone;
	@XmlElement(name = "delivery_name")
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	@XmlElement(name = "delivery_dept_no")
	public String getDelivery_dept_no() {
		return delivery_dept_no;
	}
	public void setDelivery_dept_no(String delivery_dept_no) {
		this.delivery_dept_no = delivery_dept_no;
	}
	@XmlElement(name = "delivery_dept")
	public String getDelivery_dept() {
		return delivery_dept;
	}
	public void setDelivery_dept(String delivery_dept) {
		this.delivery_dept = delivery_dept;
	}
	@XmlElement(name = "delivery_zone")
	public String getDelivery_zone() {
		return delivery_zone;
	}
	public void setDelivery_zone(String delivery_zone) {
		this.delivery_zone = delivery_zone;
	}
	
}
