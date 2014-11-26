package cn.explink.pos.bill99.xml;

import javax.xml.bind.annotation.XmlElement;

public class ext_attributes {

	private String delivery_dept_no;
	private String delivery_dept;
	private String delivery_man;
	private String delivery_name;

	private String ex_packages;

	@XmlElement(name = "ex_packages")
	public String getEx_packages() {
		return ex_packages;
	}

	public void setEx_packages(String ex_packages) {
		this.ex_packages = ex_packages;
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

	@XmlElement(name = "delivery_man")
	public String getDelivery_man() {
		return delivery_man;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}

	@XmlElement(name = "delivery_name")
	public String getDelivery_name() {
		return delivery_name;
	}

	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}

}
