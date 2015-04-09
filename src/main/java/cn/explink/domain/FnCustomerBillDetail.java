package cn.explink.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fn_customer_bill", schema = "")
@SuppressWarnings("serial")
public class FnCustomerBillDetail implements java.io.Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name="bill_id", nullable=false, length=30)
	private Long billId;

	@Column(name="order_no")
	private String orderNo;

	@Column(name="order_id")
	private Long orderId;

	@Column(name="receive_fee")
	private BigDecimal receiveFee;

	@Column(name="actual_pay")
	private BigDecimal actualPay;

	@Column(name="pay_time")
	private String payTime;

	private BigDecimal refund;

	@Column(name="actual_refund")
	private BigDecimal actualRefund;

	@Column(name="diff_amount")
	private BigDecimal diffAmount;

	@Column(name="pay_method")
	private Integer payMethod;

	private String remark;

	private String signer;

	@Column(name="sign_time")
	private String signTime;

	@Column(name="arrival_time")
	private String arrivalTime;

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getReceiveFee() {
		return receiveFee;
	}

	public void setReceiveFee(BigDecimal receiveFee) {
		this.receiveFee = receiveFee;
	}

	public BigDecimal getActualPay() {
		return actualPay;
	}

	public void setActualPay(BigDecimal actualPay) {
		this.actualPay = actualPay;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}

	public BigDecimal getActualRefund() {
		return actualRefund;
	}

	public void setActualRefund(BigDecimal actualRefund) {
		this.actualRefund = actualRefund;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
}
