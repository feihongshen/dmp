package cn.explink.domain.VO.express;

import java.math.BigDecimal;
/**
 * 统计需要入站的记录数
 * @author jiangyu 2015年8月4日
 *
 */
public class ExpressIntoStationCountVO {
	/**
	 * 支付方式【现付，到付，现结】
	 */
	private Integer payType;
	/**
	 * 统计数量
	 */
	private Long count=0L;
	/**
	 * 统计金额
	 */
	private BigDecimal sumFee=BigDecimal.ZERO;

	public ExpressIntoStationCountVO() {
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public BigDecimal getSumFee() {
		return sumFee;
	}

	public void setSumFee(BigDecimal sumFee) {
		this.sumFee = sumFee;
	}
}
