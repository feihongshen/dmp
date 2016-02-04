package cn.explink.b2c.yonghui.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {
	private String shipmentId;
	private String packageNum;
	private String orderType;
	private String recipientName;
	private String deliveryAddressProvince;
	private String deliveryAddressCity;
	//private String borough;==替换成deliveryAddressDistrict
	private String deliveryAddressDistrict;
	private String deliveryAddressDetail;
	private String recipientPhone;
	private BigDecimal commodityWeight;
	private BigDecimal totalPrice;
	private String baseStore;
	private String cashPay;
	private String deliveryTime;
	private String logistics_name;
	private String opname;
	private String stationid;
	private String prephone;
	private String packageRemark;
	private String street;//========新增

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getShipmentId() {
		return this.shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPackageNum() {
		return this.packageNum;
	}

	public void setPackageNum(String packageNum) {
		this.packageNum = packageNum;
	}

	public String getRecipientName() {
		return this.recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getDeliveryAddressProvince() {
		return this.deliveryAddressProvince;
	}

	public void setDeliveryAddressProvince(String deliveryAddressProvince) {
		this.deliveryAddressProvince = deliveryAddressProvince;
	}

	public String getDeliveryAddressCity() {
		return this.deliveryAddressCity;
	}

	public void setDeliveryAddressCity(String deliveryAddressCity) {
		this.deliveryAddressCity = deliveryAddressCity;
	}

	public String getDeliveryAddressDistrict() {
		return this.deliveryAddressDistrict;
	}

	public void setDeliveryAddressDistrict(String deliveryAddressDistrict) {
		this.deliveryAddressDistrict = deliveryAddressDistrict;
	}

	public String getDeliveryAddressDetail() {
		return this.deliveryAddressDetail;
	}

	public void setDeliveryAddressDetail(String deliveryAddressDetail) {
		this.deliveryAddressDetail = deliveryAddressDetail;
	}

	public String getRecipientPhone() {
		return this.recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public BigDecimal getCommodityWeight() {
		return this.commodityWeight;
	}

	public void setCommodityWeight(BigDecimal commodityWeight) {
		this.commodityWeight = commodityWeight;
	}

	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getBaseStore() {
		return this.baseStore;
	}

	public void setBaseStore(String baseStore) {
		this.baseStore = baseStore;
	}

	public String getCashPay() {
		return this.cashPay;
	}

	public void setCashPay(String cashPay) {
		this.cashPay = cashPay;
	}

	public String getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getLogistics_name() {
		return this.logistics_name;
	}

	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}

	public String getOpname() {
		return this.opname;
	}

	public void setOpname(String opname) {
		this.opname = opname;
	}

	public String getStationid() {
		return this.stationid;
	}

	public void setStationid(String stationid) {
		this.stationid = stationid;
	}

	public String getPrephone() {
		return this.prephone;
	}

	public void setPrephone(String prephone) {
		this.prephone = prephone;
	}

	public String getPackageRemark() {
		return this.packageRemark;
	}

	public void setPackageRemark(String packageRemark) {
		this.packageRemark = packageRemark;
	}
}
