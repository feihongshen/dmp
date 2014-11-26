package cn.explink.b2c.saohuobang.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RequestOrder")
public class RequestOrder {

	String clientID;
	String logisticProviderID;
	String txLogisticID;
	String customerId;
	String tradeNo;
	String mailNo;
	String type;
	String itemsAllPrice;
	String flag;
	String storeID;
	String storeName;
	String storeAddress;
	String storeTel;
	String supplierID;
	String supplierName;
	String orderCreateTime;
	Sender send;
	Receiver receiver;
	String sendStartTime;
	String sendEndTime;
	String itemsValue;
	String itemsWeight;
	List<Items> listitems = null;
	String insuranceValue;
	String itemsTakePrice;
	String itemsSendDate;
	String packageOrNot;
	String special;
	String remark;
	String reserve1;
	String reserve2;
	String reserve3;
	String reserve4;
	String reserve5;

	@XmlElement(name = "clientID")
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	@XmlElement(name = "logisticProviderID")
	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	@XmlElement(name = "txLogisticID")
	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	@XmlElement(name = "customerId")
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@XmlElement(name = "tradeNo")
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	@XmlElement(name = "mailNo")
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "flag")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@XmlElement(name = "storeID")
	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	@XmlElement(name = "storeName")
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@XmlElement(name = "storeAddress")
	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	@XmlElement(name = "storeTel")
	public String getStoreTel() {
		return storeTel;
	}

	public void setStoreTel(String storeTel) {
		this.storeTel = storeTel;
	}

	@XmlElement(name = "supplierID")
	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	@XmlElement(name = "supplierName")
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@XmlElement(name = "orderCreateTime")
	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	@XmlElement(name = "sender")
	public Sender getSend() {
		return send;
	}

	public void setSend(Sender send) {
		this.send = send;
	}

	@XmlElement(name = "receiver")
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@XmlElement(name = "sendStartTime")
	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	@XmlElement(name = "sendEndTime")
	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	@XmlElement(name = "itemsValue")
	public String getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(String itemsValue) {
		this.itemsValue = itemsValue;
	}

	@XmlElement(name = "itemsWeight")
	public String getItemsWeight() {
		return itemsWeight;
	}

	public void setItemsWeight(String itemsWeight) {
		this.itemsWeight = itemsWeight;
	}

	@XmlElement(name = "items")
	public List<Items> getListitems() {
		return listitems;
	}

	public void setListitems(List<Items> listitems) {
		this.listitems = listitems;
	}

	@XmlElement(name = "insuranceValue")
	public String getInsuranceValue() {
		return insuranceValue;
	}

	public void setInsuranceValue(String insuranceValue) {
		this.insuranceValue = insuranceValue;
	}

	@XmlElement(name = "packageOrNot")
	public String getPackageOrNot() {
		return packageOrNot;
	}

	public void setPackageOrNot(String packageOrNot) {
		this.packageOrNot = packageOrNot;
	}

	@XmlElement(name = "special")
	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "reserve1")
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	@XmlElement(name = "reserve2")
	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	@XmlElement(name = "reserve3")
	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	@XmlElement(name = "reserve4")
	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	@XmlElement(name = "reserve5")
	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	@XmlElement(name = "itemsAllPrice")
	public String getItemsAllPrice() {
		return itemsAllPrice;
	}

	public void setItemsAllPrice(String itemsAllPrice) {
		this.itemsAllPrice = itemsAllPrice;
	}

	public String getItemsTakePrice() {
		return itemsTakePrice;
	}

	public void setItemsTakePrice(String itemsTakePrice) {
		this.itemsTakePrice = itemsTakePrice;
	}

	public String getItemsSendDate() {
		return itemsSendDate;
	}

	public void setItemsSendDate(String itemsSendDate) {
		this.itemsSendDate = itemsSendDate;
	}

}
