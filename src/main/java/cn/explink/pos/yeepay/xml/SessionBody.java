package cn.explink.pos.yeepay.xml;

import javax.xml.bind.annotation.XmlElement;

public class SessionBody {

	ExtendAtt extendAtt;

	String employee_ID;
	String password;

	@XmlElement(name = "Password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	String order_No;

	@XmlElement(name = "Employee_ID")
	public String getEmployee_ID() {
		return employee_ID;
	}

	public void setEmployee_ID(String employee_ID) {
		this.employee_ID = employee_ID;
	}

	@XmlElement(name = "Order_No")
	public String getOrder_No() {
		return order_No;
	}

	public void setOrder_No(String order_No) {
		this.order_No = order_No;
	}

	@XmlElement(name = "ExtendAtt")
	public ExtendAtt getExtendAtt() {
		return extendAtt;
	}

	public void setExtendAtt(ExtendAtt extendAtt) {
		this.extendAtt = extendAtt;
	}

}
