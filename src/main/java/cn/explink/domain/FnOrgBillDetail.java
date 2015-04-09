package cn.explink.domain;

import java.math.BigDecimal;
/**
 * 账单明细表
 * 
 * @author 140624
 *
 */
public class FnOrgBillDetail {
	
	private Long id;//订单明细ID
	private Long billId;//账单ID
	private String orderNo;//订单号
	private String orderId;//订单ID
	private Integer status;//收款状态
	private Integer payMethod;//收款类型
	private String picker;//小件员
	private BigDecimal goodsAmount;//货款金额
	private BigDecimal verifyAmount;//核销金额
	private Long rechargesId;//充值记录编号
	private String signTime;//签收日期
	private String remark;//备注
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}
	public String getPicker() {
		return picker;
	}
	public void setPicker(String picker) {
		this.picker = picker;
	}
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public BigDecimal getVerifyAmount() {
		return verifyAmount;
	}
	public void setVerifyAmount(BigDecimal verifyAmount) {
		this.verifyAmount = verifyAmount;
	}
	public Long getRechargesId() {
		return rechargesId;
	}
	public void setRechargesId(Long rechargesId) {
		this.rechargesId = rechargesId;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
}
