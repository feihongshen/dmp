package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 站点机构调整单
 * @author JY
 *
 */
public class OrgBillAdjustmentRecord{
	/**
	 * 调整单id
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 账单id
	 */
	private Long billId;
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 调整单编号
	 */
	private String adjustBillNo;
	/**
	 * 客户
	 */
	private Long customerId;
	/**
	 * 应收
	 */
	private BigDecimal receiveFee;
	/**
	 * 应退
	 */
	private BigDecimal refundFee;
	/**
	 * 修改金额
	 */
	private BigDecimal modifyFee;
	/**
	 * 调整后的金额
	 */
	private BigDecimal adjustAmount;
	/**
	 * 货款金额
	 */
	private BigDecimal goodsAmount;
	/**
	 * 核销金额
	 */
	private BigDecimal verifyAmount;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 订单类型
	 */
	private Integer orderType;
	/**
	 * 支付方式
	 */
	private Integer payMethod;
	/**
	 *小件员
	 */
	private Long deliverId;
	/**
	 * 签收时间
	 */
	private Date signTime;
	/**
	 * 付款状态
	 */
	private Integer status;
	
	/**
	 * 目的站
	 */
	private Long deliverybranchid;
	
	public OrgBillAdjustmentRecord() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAdjustBillNo() {
		return adjustBillNo;
	}

	public void setAdjustBillNo(String adjustBillNo) {
		this.adjustBillNo = adjustBillNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getReceiveFee() {
		return receiveFee;
	}

	public void setReceiveFee(BigDecimal receiveFee) {
		this.receiveFee = receiveFee;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	public BigDecimal getModifyFee() {
		return modifyFee;
	}

	public void setModifyFee(BigDecimal modifyFee) {
		this.modifyFee = modifyFee;
	}

	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getVerifyAmount() {
		return verifyAmount;
	}

	public void setVerifyAmount(BigDecimal verifyAmount) {
		this.verifyAmount = verifyAmount;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public Long getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(Long deliverId) {
		this.deliverId = deliverId;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(Long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
}
