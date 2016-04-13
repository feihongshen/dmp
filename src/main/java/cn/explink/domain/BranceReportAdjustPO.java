package cn.explink.domain;

import java.math.BigDecimal;

public class BranceReportAdjustPO {
	   private Long id ;
	   private Long customerId ; // 客户编号
	   private String orderNumber ; // 订单号
	   private int orderType ; // 订单类型
	   private String orderTypeDesc ; // 订单类型描述
	   private long orderStatus ;  // 订单状态
	   private String orderStatusDesc ; // 订单状态描述
	   private long operateStatus ; // 订单操作状态
	   private String operateStatusDesc ; // 订单操作状态描述
	   private Long branchId ; // 配送站点id
	   private String branchName ; // 配送站点名称
	   private BigDecimal receivableAmount; // 应收金额
	   private BigDecimal payAmount ; // 应付金额
	   private int payType ; // 支付方式
	   private String payTypeDesc ; // 支付方式描述
	   private BigDecimal adjustAmount ; // 调整金额
	   private String createTime ; // 创建时间
	   
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public long getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(long orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	public long getOperateStatus() {
		return operateStatus;
	}
	public void setOperateStatus(long operateStatus) {
		this.operateStatus = operateStatus;
	}
	public String getOperateStatusDesc() {
		return operateStatusDesc;
	}
	public void setOperateStatusDesc(String operateStatusDesc) {
		this.operateStatusDesc = operateStatusDesc;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public BigDecimal getReceivableAmount() {
		return receivableAmount;
	}
	public void setReceivableAmount(BigDecimal receivableAmount) {
		this.receivableAmount = receivableAmount;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getPayTypeDesc() {
		return payTypeDesc;
	}
	public void setPayTypeDesc(String payTypeDesc) {
		this.payTypeDesc = payTypeDesc;
	}
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
