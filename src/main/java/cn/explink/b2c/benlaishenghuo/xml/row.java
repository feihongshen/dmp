package cn.explink.b2c.benlaishenghuo.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class row {

	String waybillCode;
	String order_no;
	String shippName;
	String order_type;
	String shippDate;
	String deliveryName;
	String deliveryPhone;
	String deliveryAddress;
	String deliveryhouseId;
	String deliveryhouseName;
	String customerName;
	String customerPhone;
	String customerProvince;
	String customerCity;
	String customerAddress;
	String deliveryGoods;
	String deliveryAmount;
	String weight;
	String shouldReceive;
	String remark;
	String request;
	String goodsNum;
	String subPackageNo;

	@XmlElement(name = "waybillCode")
	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	@XmlElement(name = "order_no")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@XmlElement(name = "shippName")
	public String getShippName() {
		return shippName;
	}

	public void setShippName(String shippName) {
		this.shippName = shippName;
	}

	@XmlElement(name = "order_type")
	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	@XmlElement(name = "shippDate")
	public String getShippDate() {
		return shippDate;
	}

	public void setShippDate(String shippDate) {
		this.shippDate = shippDate;
	}

	@XmlElement(name = "deliveryName")
	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	@XmlElement(name = "deliveryPhone")
	public String getDeliveryPhone() {
		return deliveryPhone;
	}

	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}

	@XmlElement(name = "deliveryAddress")
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	@XmlElement(name = "deliveryhouseId")
	public String getDeliveryhouseId() {
		return deliveryhouseId;
	}

	public void setDeliveryhouseId(String deliveryhouseId) {
		this.deliveryhouseId = deliveryhouseId;
	}

	@XmlElement(name = "deliveryhouseName")
	public String getDeliveryhouseName() {
		return deliveryhouseName;
	}

	public void setDeliveryhouseName(String deliveryhouseName) {
		this.deliveryhouseName = deliveryhouseName;
	}

	@XmlElement(name = "customerName")
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@XmlElement(name = "customerPhone")
	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	@XmlElement(name = "customerProvince")
	public String getCustomerProvince() {
		return customerProvince;
	}

	public void setCustomerProvince(String customerProvince) {
		this.customerProvince = customerProvince;
	}

	@XmlElement(name = "customerCity")
	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	@XmlElement(name = "customerAddress")
	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	@XmlElement(name = "deliveryGoods")
	public String getDeliveryGoods() {
		return deliveryGoods;
	}

	public void setDeliveryGoods(String deliveryGoods) {
		this.deliveryGoods = deliveryGoods;
	}

	@XmlElement(name = "deliveryAmount")
	public String getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	@XmlElement(name = "weight")
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@XmlElement(name = "shouldReceive")
	public String getShouldReceive() {
		return shouldReceive;
	}

	public void setShouldReceive(String shouldReceive) {
		this.shouldReceive = shouldReceive;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "request")
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	@XmlElement(name = "goodsNum")
	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	@XmlElement(name = "subPackageNo")
	public String getSubPackageNo() {
		return subPackageNo;
	}

	public void setSubPackageNo(String subPackageNo) {
		this.subPackageNo = subPackageNo;
	}

}
