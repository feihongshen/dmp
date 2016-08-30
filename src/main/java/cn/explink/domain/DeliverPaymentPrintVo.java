package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

import cn.explink.enumutil.DeliveryPaymentPatternEnum;

/**
 * 交款单打印VO
 * @author chunlei05.li
 * @date 2016年8月29日 下午6:34:12
 */
public class DeliverPaymentPrintVo {
	
	/**
	 * 打印列表
	 */
	private List<DeliverPaymentReportVo> deliverPaymentReportVoList;
	
	/**
	 * 交款方式
	 */
	private DeliveryPaymentPatternEnum deliveryPayment;
	
	/**
	 * 订单数量
	 */
	private int orderCount;
	
	/**
	 * 应收合计
	 */
	private BigDecimal shouldTotal  = BigDecimal.ZERO;
	
	/**
	 * 实收合计
	 */
	private BigDecimal realTotal  = BigDecimal.ZERO;

	public List<DeliverPaymentReportVo> getDeliverPaymentReportVoList() {
		return deliverPaymentReportVoList;
	}

	public void setDeliverPaymentReportVoList(List<DeliverPaymentReportVo> deliverPaymentReportVoList) {
		this.deliverPaymentReportVoList = deliverPaymentReportVoList;
	}

	public DeliveryPaymentPatternEnum getDeliveryPayment() {
		return deliveryPayment;
	}

	public void setDeliveryPayment(DeliveryPaymentPatternEnum deliveryPayment) {
		this.deliveryPayment = deliveryPayment;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
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

	@Override
	public String toString() {
		return "DeliverPaymentPrintVo [deliverPaymentReportVoList=" + deliverPaymentReportVoList + ", deliveryPayment="
				+ deliveryPayment + ", orderCount=" + orderCount + ", shouldTotal=" + shouldTotal + ", realTotal="
				+ realTotal + "]";
	}
}
