package cn.explink.b2c.zhongliang.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Response_Order {
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
	private String packweight = "";
	private String taxflag = "";//
	private String taxtype = "";//
	private String valuableflag = "";//
	private String stockoutdate = "";
	private String orderTemark = "";//
	private String boxes = "";//
	private String ordervalue = "";//
	private String getvalue = "";//
	private String paytype = "";//
	private String freshflag = "";//
	private String sendtime = "";//
	private String provincename = "";//
	private String cityname = "";//
	private String areaname = "";//
	private String orderstatus = "";//
	private String returntaxvalue = "";//
	private String purlevel = "";//
	private String returntaxflag = "";//
	private List<Response_PackageDetail> packageDetail = new ArrayList<Response_PackageDetail>();//
	private List<Response_GoodDetail> goodDetail = new ArrayList<Response_GoodDetail>();

	@XmlElement(name = "PurchaseNO")
	public String getPurchaseNO() {
		return PurchaseNO;
	}

	public void setPurchaseNO(String purchaseNO) {
		PurchaseNO = purchaseNO;
	}

	@XmlElement(name = "SendOrderID")
	public String getSendOrderID() {
		return SendOrderID;
	}

	public void setSendOrderID(String sendOrderID) {
		SendOrderID = sendOrderID;
	}

	@XmlElement(name = "PackageDetail")
	public List<Response_PackageDetail> getPackageDetail() {
		return packageDetail;
	}

	public void setPackageDetail(List<Response_PackageDetail> packageDetail) {
		this.packageDetail = packageDetail;
	}

	@XmlElement(name = "GoodDetail")
	public List<Response_GoodDetail> getGoodDetail() {
		return goodDetail;
	}

	public void setGoodDetail(List<Response_GoodDetail> goodDetail) {
		this.goodDetail = goodDetail;
	}

	@XmlElement(name = "OrderID")
	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	@XmlElement(name = "OrderType")
	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	@XmlElement(name = "WAREHOUSENAME")
	public String getWAREHOUSENAME() {
		return WAREHOUSENAME;
	}

	public void setWAREHOUSENAME(String wAREHOUSENAME) {
		WAREHOUSENAME = wAREHOUSENAME;
	}

	@XmlElement(name = "linkMan")
	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@XmlElement(name = "TelNO")
	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	@XmlElement(name = "HandsetNO")
	public String getHandsetno() {
		return handsetno;
	}

	public void setHandsetno(String handsetno) {
		this.handsetno = handsetno;
	}

	@XmlElement(name = "Address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlElement(name = "PostalCode")
	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@XmlElement(name = "ExpCompany")
	public String getExpcompany() {
		return expcompany;
	}

	public void setExpcompany(String expcompany) {
		this.expcompany = expcompany;
	}

	@XmlElement(name = "Freight")
	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	@XmlElement(name = "PackWeight")
	public String getPackweight() {
		return packweight;
	}

	public void setPackweight(String packweight) {
		this.packweight = packweight;
	}

	@XmlElement(name = "TaxFlag")
	public String getTaxflag() {
		return taxflag;
	}

	public void setTaxflag(String taxflag) {
		this.taxflag = taxflag;
	}

	@XmlElement(name = "TaxType")
	public String getTaxtype() {
		return taxtype;
	}

	public void setTaxtype(String taxtype) {
		this.taxtype = taxtype;
	}

	@XmlElement(name = "ValuableFlag")
	public String getValuableflag() {
		return valuableflag;
	}

	public void setValuableflag(String valuableflag) {
		this.valuableflag = valuableflag;
	}

	@XmlElement(name = "StockoutDate")
	public String getStockoutdate() {
		return stockoutdate;
	}

	public void setStockoutdate(String stockoutdate) {
		this.stockoutdate = stockoutdate;
	}

	@XmlElement(name = "OrderTemark")
	public String getOrderTemark() {
		return orderTemark;
	}

	public void setOrderTemark(String orderTemark) {
		this.orderTemark = orderTemark;
	}

	@XmlElement(name = "Boxes")
	public String getBoxes() {
		return boxes;
	}

	public void setBoxes(String boxes) {
		this.boxes = boxes;
	}

	@XmlElement(name = "OrderValue")
	public String getOrdervalue() {
		return ordervalue;
	}

	public void setOrdervalue(String ordervalue) {
		this.ordervalue = ordervalue;
	}

	@XmlElement(name = "GetValue")
	public String getGetvalue() {
		return getvalue;
	}

	public void setGetvalue(String getvalue) {
		this.getvalue = getvalue;
	}

	@XmlElement(name = "PayType")
	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	@XmlElement(name = "FreshFlag")
	public String getFreshflag() {
		return freshflag;
	}

	public void setFreshflag(String freshflag) {
		this.freshflag = freshflag;
	}

	@XmlElement(name = "SendTime")
	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	@XmlElement(name = "ProvinceName")
	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	@XmlElement(name = "CityName")
	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	@XmlElement(name = "AreaName")
	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	@XmlElement(name = "OrderStatus")
	public String getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	@XmlElement(name = "ReturnTaxValue")
	public String getReturntaxvalue() {
		return returntaxvalue;
	}

	public void setReturntaxvalue(String returntaxvalue) {
		this.returntaxvalue = returntaxvalue;
	}

	@XmlElement(name = "PurLevel")
	public String getPurlevel() {
		return purlevel;
	}

	public void setPurlevel(String purlevel) {
		this.purlevel = purlevel;
	}

	@XmlElement(name = "ReturnTaxFlag")
	public String getReturntaxflag() {
		return returntaxflag;
	}

	public void setReturntaxflag(String returntaxflag) {
		this.returntaxflag = returntaxflag;
	}

}
