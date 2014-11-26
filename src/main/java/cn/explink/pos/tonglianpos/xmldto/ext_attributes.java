package cn.explink.pos.tonglianpos.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class ext_attributes {
	private String delivery_dept_no;
	private String delivery_dept;
	private String delivery_man;
	private String delivery_name;

	private int ex_packages;
	private String consignee;
	private String consignee_address;
	private String consignee_contact;

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

	@XmlElement(name = "ex_packages")
	public int getEx_packages() {
		return ex_packages;
	}

	public void setEx_packages(int ex_packages) {
		this.ex_packages = ex_packages;
	}

	@XmlElement(name = "consignee")
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@XmlElement(name = "consignee_address")
	public String getConsignee_address() {
		return consignee_address;
	}

	public void setConsignee_address(String consignee_address) {
		this.consignee_address = consignee_address;
	}

	@XmlElement(name = "consignee_contact")
	public String getConsignee_contact() {
		return consignee_contact;
	}

	public void setConsignee_contact(String consignee_contact) {
		this.consignee_contact = consignee_contact;
	}

}
