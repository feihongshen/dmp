package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 缴款单报表
 * @author chunlei05.li
 * @date 2016年8月25日 下午6:37:07
 */
public class DeliverPaymentPrintVo {
	
	/**
	 * 小件员名称
	 */
	private String deliveryName;
	
	/**
	 * 支付类型
	 */
	private int deliveryPaymentPatternId;
	
	/**
	 * 支付类型
	 */
	private String deliveryPaymentPattern;
	
	/**
	 * 供应商id
	 */
	private long customerId;
	
	/**
	 * 供应商名称
	 */
	private String customerName;
	
	/**
	 * 订单类型
	 */
	private int cwbOrderTypeId;
	
	/**
	 * 订单类型
	 */
	private String cwbOrderType;
	
	/**
	 * 订单数量
	 */
	private int orderCount;
	
	/**
	 * 应收金额
	 */
	private BigDecimal shouldReceivedfee = BigDecimal.ZERO;
	
	public String getShouldReceivedfeeStr() {
		BigDecimal newShouldReceivedfee = shouldReceivedfee.setScale(2);
		return newShouldReceivedfee.toString();
	}
	
	/**
	 * 应退金额
	 */
	private BigDecimal shouldPaybackfee = BigDecimal.ZERO;
	
	public String getShouldPaybackfeeStr() {
		BigDecimal newShouldPaybackfee = shouldPaybackfee.setScale(2);
		return newShouldPaybackfee.toString();
	}
	
	/**
	 * 应收运费
	 */
	private BigDecimal shouldfare  = BigDecimal.ZERO;
	
	public String getShouldfareStr() {
		BigDecimal newShouldfare = shouldfare.setScale(2);
		return newShouldfare.toString();
	}
	
	/**
	 * 应收合计
	 */
	private BigDecimal shouldTotal  = BigDecimal.ZERO;
	
	public String getShouldTotalStr() {
		BigDecimal newShouldTotal = shouldTotal.setScale(2);
		return newShouldTotal.toString();
	}
	
	/**
	 * 实收合计
	 */
	private BigDecimal realTotal  = BigDecimal.ZERO;
	
	public String getRealTotalStr() {
		BigDecimal newRealTotal = realTotal.setScale(2);
		return newRealTotal.toString();
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryPaymentPattern() {
		return deliveryPaymentPattern;
	}

	public void setDeliveryPaymentPattern(String deliveryPaymentPattern) {
		this.deliveryPaymentPattern = deliveryPaymentPattern;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCwbOrderType() {
		return cwbOrderType;
	}

	public void setCwbOrderType(String cwbOrderType) {
		this.cwbOrderType = cwbOrderType;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public BigDecimal getShouldReceivedfee() {
		return shouldReceivedfee;
	}

	public void setShouldReceivedfee(BigDecimal shouldReceivedfee) {
		this.shouldReceivedfee = shouldReceivedfee;
	}

	public BigDecimal getShouldPaybackfee() {
		return shouldPaybackfee;
	}

	public void setShouldPaybackfee(BigDecimal shouldPaybackfee) {
		this.shouldPaybackfee = shouldPaybackfee;
	}

	public BigDecimal getShouldfare() {
		return shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public BigDecimal getShouldTotal() {
		return shouldTotal;
	}

	public void setShouldTotal(BigDecimal shouldTotal) {
		this.shouldTotal = shouldTotal;
	}

	public BigDecimal getRealTotal() {
		return realTotal;
	}

	public void setRealTotal(BigDecimal realTotal) {
		this.realTotal = realTotal;
	}

	public int getDeliveryPaymentPatternId() {
		return deliveryPaymentPatternId;
	}

	public void setDeliveryPaymentPatternId(int deliveryPaymentPatternId) {
		this.deliveryPaymentPatternId = deliveryPaymentPatternId;
	}

	public int getCwbOrderTypeId() {
		return cwbOrderTypeId;
	}

	public void setCwbOrderTypeId(int cwbOrderTypeId) {
		this.cwbOrderTypeId = cwbOrderTypeId;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "DeliverPaymentPrintVo [deliveryName=" + deliveryName + ", deliveryPaymentPatternId="
				+ deliveryPaymentPatternId + ", deliveryPaymentPattern=" + deliveryPaymentPattern + ", customerId="
				+ customerId + ", customerName=" + customerName + ", cwbOrderTypeId=" + cwbOrderTypeId
				+ ", cwbOrderType=" + cwbOrderType + ", orderCount=" + orderCount + ", shouldReceivedfee="
				+ shouldReceivedfee + ", shouldPaybackfee=" + shouldPaybackfee + ", shouldfare=" + shouldfare
				+ ", shouldTotal=" + shouldTotal + ", realTotal=" + realTotal + "]";
	}
}
