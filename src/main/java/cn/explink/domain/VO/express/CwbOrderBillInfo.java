package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 生成账单的时候查询订单的部分信息封装
 * @author jiangyu 2015年8月11日
 *
 */
public class CwbOrderBillInfo {
	
	private Long id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 订单件数
	 */
	private Long orderCount;
	/**
	 * 揽件员
	 */
	private String deliveryMan;
	/**
	 * 派件员
	 */
	private String sendMan;
	/**
	 * 运费合计
	 */
	private BigDecimal transportFeeTotal;
	/**
	 * 运费
	 */
	private BigDecimal transportFee;
	/**
	 * 包装费
	 */
	private BigDecimal packFee;
	/**
	 * 保价费
	 */
	private BigDecimal saveFee;
	/**
	 * 代收货款
	 */
	private BigDecimal receivablefee;
	/**
	 * 付款方式
	 */
	private String payMethod;
	
	/**
	 * 站点
	 */
	private String branchName;
	/**
	 * 导入应收账单的时候没有匹配的原因
	 */
	private String notMatchReason;
	/**
	 * 站点id
	 */
	private Long branchId;
	/**
	 * 对应数据的样式 默认是黑色
	 */
	private String trColor="black";
	
	public CwbOrderBillInfo() {
		// TODO Auto-generated constructor stub
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

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public String getDeliveryMan() {
		return deliveryMan;
	}

	public void setDeliveryMan(String deliveryMan) {
		this.deliveryMan = deliveryMan;
	}

	public String getSendMan() {
		return sendMan;
	}

	public void setSendMan(String sendMan) {
		this.sendMan = sendMan;
	}

	public BigDecimal getTransportFeeTotal() {
		if (null==transportFeeTotal) {
			transportFeeTotal = BigDecimal.ZERO;
		}
		return transportFeeTotal;
	}

	public void setTransportFeeTotal(BigDecimal transportFeeTotal) {
		this.transportFeeTotal = transportFeeTotal;
	}

	public BigDecimal getTransportFee() {
		if (null==transportFee) {
			transportFee = BigDecimal.ZERO;
		}
		return transportFee;
	}

	public void setTransportFee(BigDecimal transportFee) {
		this.transportFee = transportFee;
	}

	public BigDecimal getPackFee() {
		if (null==packFee) {
			packFee = BigDecimal.ZERO;
		}
		return packFee;
	}

	public void setPackFee(BigDecimal packFee) {
		this.packFee = packFee;
	}

	public BigDecimal getSaveFee() {
		if (null==saveFee) {
			saveFee = BigDecimal.ZERO;
		}
		return saveFee;
	}

	public void setSaveFee(BigDecimal saveFee) {
		this.saveFee = saveFee;
	}

	public BigDecimal getReceivablefee() {
		if (null==receivablefee) {
			receivablefee = BigDecimal.ZERO;
		}
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getNotMatchReason() {
		return notMatchReason;
	}

	public void setNotMatchReason(String notMatchReason) {
		this.notMatchReason = notMatchReason;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getTrColor() {
		return trColor;
	}

	public void setTrColor(String trColor) {
		this.trColor = trColor;
	}
}
