package cn.explink.b2c.feiniuwang;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CwbRequest {
	@JsonProperty(value = "eccompanyid") 
	private String eccompanyid;
	@JsonProperty(value = "logisticproviderid")
	private String logisticproviderid;
	@JsonProperty(value = "customerid")
	private String customerid;
	@JsonProperty(value = "txlogisticid")
	private String txlogisticid;
	@JsonProperty(value = "tradeno")
	private String tradeno;
	@JsonProperty(value = "mailno")
	private String mailno;
	@JsonProperty(value = "returnno")
	private String returnno;
	@JsonProperty(value = "totalservicefee")
	private String totalservicefee;
	@JsonProperty(value = "codsplitfee")
	private String codsplitfee;
	@JsonProperty(value = "buyservicefee")
	private String buyservicefee;
	@JsonProperty(value = "ordertype")
	private String ordertype;
	@JsonProperty(value = "servicetype")
	private String servicetype;
	@JsonProperty(value = "sender")
	private Sender sender;
	@JsonProperty(value = "receiver")
	private Receiver receiver;
	@JsonProperty(value = "createordertime")
	private String createordertime;
	@JsonProperty(value = "sendstarttime")
	private String sendstarttime;
	@JsonProperty(value = "sendendtime")
	private String sendendtime;
	@JsonProperty(value = "goodsvalue")
	private String goodsvalue;
	@JsonProperty(value = "itemsvalue")
	private String itemsvalue;
	@JsonProperty(value = "items")
	private List<Items> items;
	@JsonProperty(value = "special")
	private String special;
	@JsonProperty(value = "stockname")
	private String stockname;
	@JsonProperty(value = "paytype")
	private String paytype;
	@JsonProperty(value = "weight")
	private String weight;
	@JsonProperty(value = "dispatcharea")
	private String dispatcharea;
	@JsonProperty(value = "remark")
	private String remark;
	@JsonProperty(value = "othersite")
	private String othersite;
	@JsonProperty(value = "deliverydate")
	private String deliverydate;
	@JsonProperty(value = "deliverybegindate")
	private String deliverybegindate;
	@JsonProperty(value = "deliveryenddate")
	private String deliveryenddate;
	public String getOthersite() {
		return othersite;
	}
	public void setOthersite(String othersite) {
		this.othersite = othersite;
	}
	public String getDeliverydate() {
		return deliverydate;
	}
	public void setDeliverydate(String deliverydate) {
		this.deliverydate = deliverydate;
	}
	public String getDeliverybegindate() {
		return deliverybegindate;
	}
	public void setDeliverybegindate(String deliverybegindate) {
		this.deliverybegindate = deliverybegindate;
	}
	public String getDeliveryenddate() {
		return deliveryenddate;
	}
	public void setDeliveryenddate(String deliveryenddate) {
		this.deliveryenddate = deliveryenddate;
	}
	

	public String getEccompanyid() {
		return eccompanyid;
	}
	public void setEccompanyid(String eccompanyid) {
		this.eccompanyid = eccompanyid;
	}
	public String getLogisticproviderid() {
		return logisticproviderid;
	}
	public void setLogisticproviderid(String logisticproviderid) {
		this.logisticproviderid = logisticproviderid;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getTxlogisticid() {
		return txlogisticid;
	}
	public void setTxlogisticid(String txlogisticid) {
		this.txlogisticid = txlogisticid;
	}
	public String getTradeno() {
		return tradeno;
	}
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public String getReturnno() {
		return returnno;
	}
	public void setReturnno(String returnno) {
		this.returnno = returnno;
	}
	
	public String getCreateordertime() {
		return createordertime;
	}
	public void setCreateordertime(String createordertime) {
		this.createordertime = createordertime;
	}
	public String getSendstarttime() {
		return sendstarttime;
	}
	public void setSendstarttime(String sendstarttime) {
		this.sendstarttime = sendstarttime;
	}
	public String getSendendtime() {
		return sendendtime;
	}
	public void setSendendtime(String sendendtime) {
		this.sendendtime = sendendtime;
	}
	
	
	public String getItemsvalue() {
		return itemsvalue;
	}
	public void setItemsvalue(String itemsvalue) {
		this.itemsvalue = itemsvalue;
	}
	
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getGoodsvalue() {
		return goodsvalue;
	}
	public void setGoodsvalue(String goodsvalue) {
		this.goodsvalue = goodsvalue;
	}
	public String getTotalservicefee() {
		return totalservicefee;
	}
	public void setTotalservicefee(String totalservicefee) {
		this.totalservicefee = totalservicefee;
	}
	public String getBuyservicefee() {
		return buyservicefee;
	}
	public void setBuyservicefee(String buyservicefee) {
		this.buyservicefee = buyservicefee;
	}
	public String getCodsplitfee() {
		return codsplitfee;
	}
	public void setCodsplitfee(String codsplitfee) {
		this.codsplitfee = codsplitfee;
	}
	
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getDispatcharea() {
		return dispatcharea;
	}
	public void setDispatcharea(String dispatcharea) {
		this.dispatcharea = dispatcharea;
	}
	public Sender getSender() {
		return sender;
	}
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	public Receiver getReceiver() {
		return receiver;
	}
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	public List<Items> getItems() {
		return items;
	}
	public void setItems(List<Items> items) {
		this.items = items;
	}
	
}
