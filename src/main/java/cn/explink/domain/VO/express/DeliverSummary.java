/**
 *
 */
package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 小件员交件汇总单
 *
 * @author songkaojun 2015年8月9日
 */
public class DeliverSummary {
	private long deliverid;
	/**
	 * 单量
	 */
	private long orderSum = 0L;
	private BigDecimal totalfeeSum = new BigDecimal(0);
	private int paymethod;

	public long getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public long getOrderSum() {
		return this.orderSum;
	}

	public void setOrderSum(long orderSum) {
		this.orderSum = orderSum;
	}

	public BigDecimal getTotalfeeSum() {
		return this.totalfeeSum;
	}

	public void setTotalfeeSum(BigDecimal totalfeeSum) {
		this.totalfeeSum = totalfeeSum;
	}

	public int getPaymethod() {
		return this.paymethod;
	}

	public void setPaymethod(int paymethod) {
		this.paymethod = paymethod;
	}

}
