package cn.explink.domain;

public class EmsOrderPO {
	
	private String orderNumber ; // 订单编号
	private int orderType ; // 订单类型
	private String orderTypeDesc ; // 订单类型
	private int orderStatus ; // 订单状态
	private String orderStatusDesc ; // 订单状态
	private int orderCurrentStatus ; // 订单当前状态   是指订单的操作类型
	private String orderCurrentStatusDesc ; // 订单当前状态
	private String branchName ; // 配送站点
	private String deliveryTime ; // 发货时间 
	private String deliveryCustomer ; // 发货客户
	private String recipientAddress ; // 收货人地址
	private String recipientMobile ; // 收货人手机
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getOrderTypeDesc() {
		return orderTypeDesc;
	}
	public void setOrderTypeDesc(String orderTypeDesc) {
		this.orderTypeDesc = orderTypeDesc;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	public int getOrderCurrentStatus() {
		return orderCurrentStatus;
	}
	public void setOrderCurrentStatus(int orderCurrentStatus) {
		this.orderCurrentStatus = orderCurrentStatus;
	}
	public String getOrderCurrentStatusDesc() {
		return orderCurrentStatusDesc;
	}
	public void setOrderCurrentStatusDesc(String orderCurrentStatusDesc) {
		this.orderCurrentStatusDesc = orderCurrentStatusDesc;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getDeliveryCustomer() {
		return deliveryCustomer;
	}
	public void setDeliveryCustomer(String deliveryCustomer) {
		this.deliveryCustomer = deliveryCustomer;
	}
	public String getRecipientAddress() {
		return recipientAddress;
	}
	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}
	public String getRecipientMobile() {
		return recipientMobile;
	}
	public void setRecipientMobile(String recipientMobile) {
		this.recipientMobile = recipientMobile;
	}

}
