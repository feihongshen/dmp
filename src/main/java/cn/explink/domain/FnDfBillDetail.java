package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

public class FnDfBillDetail {
	private long id;
	private String orderNo;//订单号
	private long billId;//账单主键
	private int   orderStatus;//订单状态
	private int   orderType;//订单类型
	private String site;//订单做归班反馈的站点
	private int deliveryNum;//发货件数
	private BigDecimal volume;//订单体积
	private BigDecimal weight;//订单重量
	private String userAddress;//收件人地址
	private BigDecimal deliveryAmount;//按派费协议计算出的订单派费
	private Date insiteTime;//订单做站点到货的操作时间
	private Date auditingTime;//订单做归班审核的操作时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public long getBillId() {
		return billId;
	}
	public void setBillId(long billId) {
		this.billId = billId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getDeliveryNum() {
		return deliveryNum;
	}
	public void setDeliveryNum(int deliveryNum) {
		this.deliveryNum = deliveryNum;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}
	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	public Date getInsiteTime() {
		return insiteTime;
	}
	public void setInsiteTime(Date insiteTime) {
		this.insiteTime = insiteTime;
	}
	public Date getAuditingTime() {
		return auditingTime;
	}
	public void setAuditingTime(Date auditingTime) {
		this.auditingTime = auditingTime;
	}
}
