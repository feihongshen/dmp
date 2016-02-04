package cn.explink.b2c.huanqiugou.reqDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



public class Order {
	private String orderno;
	private String mailno;
	private String submailno;
	private String senddate;
	private String sendcus;
	private String sendperson;
	private String sendtel;
	private String receivecus;
	private String receiveperson;
	private String receivetel;
	private String goodsname;
	private String qty;
	private String inputdate;
	private String inputperson;
	private String inputsite;
	private String lasteditdate;
	private String lasteditperson;
	private String lasteditsite;
	private String remark;
	
	private String receiveprovince;
	private String receivecity;
	private String receivearea;
	private String receiveaddress;
	private String sendprovince;
	private String sendcity;
	private String sendarea;
	private String sendaddress;
	private String productcode;
	private String weight;
	
	private String skulength;
	private String skuwidth;
	private String skuhigh;
	private String sendpcode;
	private String sendccode;
	private String sendacode;
	private String receivepcode;
	private String receiveccode;
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	private String receiveacode;
	
	private String ordertype;
	private String goodstype;
	private String warehouseid;
	private String warehouse;
	private String price;
	private String insured;
	private String payamount;
	private String paytype;
	private String paymethods;
	
	private String receiveTime;
	private String tpcontrol;
	private String udf1;
	private String udf2;
	private String udf3;
	private String udf4;
	private String udf5;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public String getSubmailno() {
		return submailno;
	}
	public void setSubmailno(String submailno) {
		this.submailno = submailno;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getSendcus() {
		return sendcus;
	}
	public void setSendcus(String sendcus) {
		this.sendcus = sendcus;
	}
	public String getSendperson() {
		return sendperson;
	}
	public void setSendperson(String sendperson) {
		this.sendperson = sendperson;
	}
	public String getSendtel() {
		return sendtel;
	}
	public void setSendtel(String sendtel) {
		this.sendtel = sendtel;
	}
	public String getReceivecus() {
		return receivecus;
	}
	public void setReceivecus(String receivecus) {
		this.receivecus = receivecus;
	}
	public String getReceiveperson() {
		return receiveperson;
	}
	public void setReceiveperson(String receiveperson) {
		this.receiveperson = receiveperson;
	}
	public String getReceivetel() {
		return receivetel;
	}
	public void setReceivetel(String receivetel) {
		this.receivetel = receivetel;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getInputdate() {
		return inputdate;
	}
	public void setInputdate(String inputdate) {
		this.inputdate = inputdate;
	}
	public String getInputperson() {
		return inputperson;
	}
	public void setInputperson(String inputperson) {
		this.inputperson = inputperson;
	}
	public String getInputsite() {
		return inputsite;
	}
	public void setInputsite(String inputsite) {
		this.inputsite = inputsite;
	}
	public String getLasteditdate() {
		return lasteditdate;
	}
	public void setLasteditdate(String lasteditdate) {
		this.lasteditdate = lasteditdate;
	}
	public String getLasteditperson() {
		return lasteditperson;
	}
	public void setLasteditperson(String lasteditperson) {
		this.lasteditperson = lasteditperson;
	}
	public String getLasteditsite() {
		return lasteditsite;
	}
	public void setLasteditsite(String lasteditsite) {
		this.lasteditsite = lasteditsite;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReceiveprovince() {
		return receiveprovince;
	}
	public void setReceiveprovince(String receiveprovince) {
		this.receiveprovince = receiveprovince;
	}
	public String getReceivecity() {
		return receivecity;
	}
	public void setReceivecity(String receivecity) {
		this.receivecity = receivecity;
	}
	public String getReceivearea() {
		return receivearea;
	}
	public void setReceivearea(String receivearea) {
		this.receivearea = receivearea;
	}
	public String getReceiveaddress() {
		return receiveaddress;
	}
	public void setReceiveaddress(String receiveaddress) {
		this.receiveaddress = receiveaddress;
	}
	public String getSendprovince() {
		return sendprovince;
	}
	public void setSendprovince(String sendprovince) {
		this.sendprovince = sendprovince;
	}
	public String getSendcity() {
		return sendcity;
	}
	public void setSendcity(String sendcity) {
		this.sendcity = sendcity;
	}
	public String getSendarea() {
		return sendarea;
	}
	public void setSendarea(String sendarea) {
		this.sendarea = sendarea;
	}
	public String getSendaddress() {
		return sendaddress;
	}
	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getSkulength() {
		return skulength;
	}
	public void setSkulength(String skulength) {
		this.skulength = skulength;
	}
	public String getSkuwidth() {
		return skuwidth;
	}
	public void setSkuwidth(String skuwidth) {
		this.skuwidth = skuwidth;
	}
	public String getSkuhigh() {
		return skuhigh;
	}
	public void setSkuhigh(String skuhigh) {
		this.skuhigh = skuhigh;
	}
	public String getSendpcode() {
		return sendpcode;
	}
	public void setSendpcode(String sendpcode) {
		this.sendpcode = sendpcode;
	}
	public String getSendccode() {
		return sendccode;
	}
	public void setSendccode(String sendccode) {
		this.sendccode = sendccode;
	}
	public String getSendacode() {
		return sendacode;
	}
	public void setSendacode(String sendacode) {
		this.sendacode = sendacode;
	}
	public String getReceivepcode() {
		return receivepcode;
	}
	public void setReceivepcode(String receivepcode) {
		this.receivepcode = receivepcode;
	}
	public String getReceiveccode() {
		return receiveccode;
	}
	public void setReceiveccode(String receiveccode) {
		this.receiveccode = receiveccode;
	}
	public String getReceiveacode() {
		return receiveacode;
	}
	public void setReceiveacode(String receiveacode) {
		this.receiveacode = receiveacode;
	}
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	public String getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(String goodstype) {
		this.goodstype = goodstype;
	}
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getInsured() {
		return insured;
	}
	public void setInsured(String insured) {
		this.insured = insured;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPaymethods() {
		return paymethods;
	}
	public void setPaymethods(String paymethods) {
		this.paymethods = paymethods;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getTpcontrol() {
		return tpcontrol;
	}
	public void setTpcontrol(String tpcontrol) {
		this.tpcontrol = tpcontrol;
	}
	public String getUdf1() {
		return udf1;
	}
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	public String getUdf2() {
		return udf2;
	}
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	public String getUdf3() {
		return udf3;
	}
	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}
	public String getUdf4() {
		return udf4;
	}
	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}
	public String getUdf5() {
		return udf5;
	}
	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}
	
}
