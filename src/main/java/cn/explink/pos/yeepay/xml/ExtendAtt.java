package cn.explink.pos.yeepay.xml;

import javax.xml.bind.annotation.XmlElement;

public class ExtendAtt {
	// SessionHeader
	String Employee_ID;
	String Company_Code;
	String Company_Name;
	String Employee_Name;

	@XmlElement(name = "Employee_Name")
	public String getEmployee_Name() {
		return Employee_Name;
	}

	public void setEmployee_Name(String employee_Name) {
		Employee_Name = employee_Name;
	}

	@XmlElement(name = "Employee_ID")
	public String getEmployee_ID() {
		return Employee_ID;
	}

	public void setEmployee_ID(String employee_ID) {
		Employee_ID = employee_ID;
	}

	@XmlElement(name = "Company_Code")
	public String getCompany_Code() {
		return Company_Code;
	}

	public void setCompany_Code(String company_Code) {
		Company_Code = company_Code;
	}

	@XmlElement(name = "Company_Name")
	public String getCompany_Name() {
		return Company_Name;
	}

	@XmlElement(name = "Except_Code")
	public String getExcept_Code() {
		return Except_Code;
	}

	public void setExcept_Code(String except_Code) {
		Except_Code = except_Code;
	}

	@XmlElement(name = "Except_Msg")
	public String getExcept_Msg() {
		return Except_Msg;
	}

	public void setExcept_Msg(String except_Msg) {
		Except_Msg = except_Msg;
	}

	public void setCompany_Name(String company_Name) {
		Company_Name = company_Name;
	}

	// SessionBody
	String OrderNo;
	String BankCardNo;
	String BankOrderNo;
	double AMT;
	int Pay_Type;
	String Bank_OrderId;
	String Yeepay_OrderId;
	String ChequeNo;
	int Sign_Self_Flag; // 签收类型 是否本人签收（1为是，0为否）
	String Sign_Name; // 签收人
	String Sign_Tel; // 签收人电话
	String Order_No;
	String Except_Code;
	String Except_Msg;
	String Except_Type; // 异常类别

	@XmlElement(name = "Except_Type")
	public String getExcept_Type() {
		return Except_Type;
	}

	public void setExcept_Type(String except_Type) {
		Except_Type = except_Type;
	}

	@XmlElement(name = "Order_No")
	public String getOrder_No() {
		return Order_No;
	}

	public void setOrder_No(String order_No) {
		Order_No = order_No;
	}

	@XmlElement(name = "Sign_Self_Flag")
	public int getSign_Self_Flag() {
		return Sign_Self_Flag;
	}

	public void setSign_Self_Flag(int sign_Self_Flag) {
		Sign_Self_Flag = sign_Self_Flag;
	}

	@XmlElement(name = "Sign_Name")
	public String getSign_Name() {
		return Sign_Name;
	}

	public void setSign_Name(String sign_Name) {
		Sign_Name = sign_Name;
	}

	@XmlElement(name = "Sign_Tel")
	public String getSign_Tel() {
		return Sign_Tel;
	}

	public void setSign_Tel(String sign_Tel) {
		Sign_Tel = sign_Tel;
	}

	@XmlElement(name = "OrderNo")
	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	@XmlElement(name = "BankCardNo")
	public String getBankCardNo() {
		return BankCardNo;
	}

	@XmlElement(name = "AMT")
	public double getAMT() {
		return AMT;
	}

	public void setAMT(double aMT) {
		AMT = aMT;
	}

	public void setBankCardNo(String bankCardNo) {
		BankCardNo = bankCardNo;
	}

	@XmlElement(name = "BankOrderNo")
	public String getBankOrderNo() {
		return BankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		BankOrderNo = bankOrderNo;
	}

	@XmlElement(name = "Pay_Type")
	public int getPay_Type() {
		return Pay_Type;
	}

	public void setPay_Type(int pay_Type) {
		Pay_Type = pay_Type;
	}

	@XmlElement(name = "Bank_OrderId")
	public String getBank_OrderId() {
		return Bank_OrderId;
	}

	public void setBank_OrderId(String bank_OrderId) {
		Bank_OrderId = bank_OrderId;
	}

	@XmlElement(name = "Yeepay_OrderId")
	public String getYeepay_OrderId() {
		return Yeepay_OrderId;
	}

	public void setYeepay_OrderId(String yeepay_OrderId) {
		Yeepay_OrderId = yeepay_OrderId;
	}

	@XmlElement(name = "ChequeNo")
	public String getChequeNo() {
		return ChequeNo;
	}

	public void setChequeNo(String chequeNo) {
		ChequeNo = chequeNo;
	}

}
