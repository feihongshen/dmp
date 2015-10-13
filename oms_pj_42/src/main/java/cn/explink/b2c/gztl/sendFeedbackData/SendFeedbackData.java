package cn.explink.b2c.gztl.sendFeedbackData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SendFeedbackData {
	/**
	 * 外发订单推送接口中所要返回的信息xml,下面带0的为可以为空的
	 */
	private String waybillNo;// 运单号
	private String subWaybillNo;// 子运单号
	private String orderNo;// 订单号
	private String custName;// 客户名称
	private String custCode;// 客户编码
	private String orderType;// 订单类型(由飞远提供)
	private String orderDate;// 订单日期
	private String deliveryman;// 配送人0
	private String deliverymanPhone;// 配送人电话0
	private String deliverymanAddress;// 配送人地址0
	private String receiverName;// 收件人姓名
	private String receiverPhone;// 收件人电话
	private String receiverAddress;// 收件人地址
	private String goodsDetail;// 配送物品0
	private String goodsNum;// 配送件数
	private String deliveryAmount;// 配送合计0
	private String weight;// 重量0
	private String receivable;// 应收款0
	private String shippedDate;// 发货时间
	private String insurAmount;// 保价 0
	private String shippedCode;// 承运商代码
	private String logisticproviderid;// 承运商ID

	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getSubWaybillNo() {
		return this.subWaybillNo;
	}

	public void setSubWaybillNo(String subWaybillNo) {
		this.subWaybillNo = subWaybillNo;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustCode() {
		return this.custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryman() {
		return this.deliveryman;
	}

	public void setDeliveryman(String deliveryman) {
		this.deliveryman = deliveryman;
	}

	public String getDeliverymanPhone() {
		return this.deliverymanPhone;
	}

	public void setDeliverymanPhone(String deliverymanPhone) {
		this.deliverymanPhone = deliverymanPhone;
	}

	public String getDeliverymanAddress() {
		return this.deliverymanAddress;
	}

	public void setDeliverymanAddress(String deliverymanAddress) {
		this.deliverymanAddress = deliverymanAddress;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return this.receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return this.receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getGoodsDetail() {
		return this.goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public String getGoodsNum() {
		return this.goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getDeliveryAmount() {
		return this.deliveryAmount;
	}

	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getReceivable() {
		return this.receivable;
	}

	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}

	public String getShippedDate() {
		return this.shippedDate;
	}

	public void setShippedDate(String shippedDate) {
		this.shippedDate = shippedDate;
	}

	public String getInsurAmount() {
		return this.insurAmount;
	}

	public void setInsurAmount(String insurAmount) {
		this.insurAmount = insurAmount;
	}

	public String getShippedCode() {
		return this.shippedCode;
	}

	public void setShippedCode(String shippedCode) {
		this.shippedCode = shippedCode;
	}

	public String getLogisticproviderid() {
		return this.logisticproviderid;
	}

	public void setLogisticproviderid(String logisticproviderid) {
		this.logisticproviderid = logisticproviderid;
	}

}
