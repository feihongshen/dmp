package cn.explink.b2c.zhongliang.backxml;

import javax.xml.bind.annotation.XmlElement;

public class Order {
	private String PurchaseNO = "";//
	private String SendOrderID = "";//
	private String WAREHOUSENAME = "";//
	private String OrderID = "";//
	private String ordertype = "";//
	private String linkman = "";//
	private String telno = "";//
	private String handsetno = "";//
	private String address = "";//
	private String postalcode = "";//
	private String expcompany;//
	private String freight = "";//
	private String ordervalue = "";//
	private String paytype = "";//
	private String freshflag = "";//
	private String sendtime = "";//
	private String provincename = "";//
	private String cityname = "";//
	private String areaname = "";//
	private String orderstatus = "";//
	private String returntaxvalue = "";//
	private String returntaxflag = "";//
	private String orderTemark = "";//
	private GoodDetail goodDetail = new GoodDetail();

	@XmlElement(name = "PurchaseNO")
	public String getPurchaseNO() {
		return this.PurchaseNO;
	}

	public void setPurchaseNO(String purchaseNO) {
		this.PurchaseNO = purchaseNO;
	}

	@XmlElement(name = "SendOrderID")
	public String getSendOrderID() {
		return this.SendOrderID;
	}

	public void setSendOrderID(String sendOrderID) {
		this.SendOrderID = sendOrderID;
	}

	@XmlElement(name = "GoodDetail")
	public GoodDetail getGoodDetail() {
		return this.goodDetail;
	}

	public void setGoodDetail(GoodDetail goodDetail) {
		this.goodDetail = goodDetail;
	}

	@XmlElement(name = "OrderID")
	public String getOrderID() {
		return this.OrderID;
	}

	public void setOrderID(String orderID) {
		this.OrderID = orderID;
	}

	@XmlElement(name = "OrderType")
	public String getOrdertype() {
		return this.ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	@XmlElement(name = "WAREHOUSENAME")
	public String getWAREHOUSENAME() {
		return this.WAREHOUSENAME;
	}

	public void setWAREHOUSENAME(String wAREHOUSENAME) {
		this.WAREHOUSENAME = wAREHOUSENAME;
	}

	@XmlElement(name = "linkMan")
	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@XmlElement(name = "TelNO")
	public String getTelno() {
		return this.telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	@XmlElement(name = "HandsetNO")
	public String getHandsetno() {
		return this.handsetno;
	}

	public void setHandsetno(String handsetno) {
		this.handsetno = handsetno;
	}

	@XmlElement(name = "Address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlElement(name = "PostalCode")
	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@XmlElement(name = "ExpCompany")
	public String getExpcompany() {
		return this.expcompany;
	}

	public void setExpcompany(String expcompany) {
		this.expcompany = expcompany;
	}

	@XmlElement(name = "Freight")
	public String getFreight() {
		return this.freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	@XmlElement(name = "OrderRemark")
	public String getOrderTemark() {
		return this.orderTemark;
	}

	public void setOrderTemark(String orderTemark) {
		this.orderTemark = orderTemark;
	}

	@XmlElement(name = "OrderValue")
	public String getOrdervalue() {
		return this.ordervalue;
	}

	public void setOrdervalue(String ordervalue) {
		this.ordervalue = ordervalue;
	}

	@XmlElement(name = "PayType")
	public String getPaytype() {
		return this.paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	@XmlElement(name = "FreshFlag")
	public String getFreshflag() {
		return this.freshflag;
	}

	public void setFreshflag(String freshflag) {
		this.freshflag = freshflag;
	}

	@XmlElement(name = "SendTime")
	public String getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	@XmlElement(name = "ProvinceName")
	public String getProvincename() {
		return this.provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	@XmlElement(name = "CityName")
	public String getCityname() {
		return this.cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	@XmlElement(name = "AreaName")
	public String getAreaname() {
		return this.areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	@XmlElement(name = "OrderStatus")
	public String getOrderstatus() {
		return this.orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	@XmlElement(name = "ReturnTaxValue")
	public String getReturntaxvalue() {
		return this.returntaxvalue;
	}

	public void setReturntaxvalue(String returntaxvalue) {
		this.returntaxvalue = returntaxvalue;
	}

	@XmlElement(name = "ReturnTaxFlag")
	public String getReturntaxflag() {
		return this.returntaxflag;
	}

	public void setReturntaxflag(String returntaxflag) {
		this.returntaxflag = returntaxflag;
	}

}
