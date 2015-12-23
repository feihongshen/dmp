package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 跨省应付的账单id
 * @author jiangyu 2015年8月20日
 *
 */
public class ExpressPayFreightBillInfo {
	/**
	 * 账单id
	 */
	private Long billId;
	/**
	 * 账单备注
	 */
	private String remark;
	/**
	 * 订单数量
	 */
	private Integer orderCount;
	/**
	 * 代收货款
	 */
	private BigDecimal receiveableFee;
	/**
	 * 运费
	 */
	private BigDecimal freightFee;
	
	
	public ExpressPayFreightBillInfo() {
		// TODO Auto-generated constructor stub
	}


	public Long getBillId() {
		return billId;
	}


	public void setBillId(Long billId) {
		this.billId = billId;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getOrderCount() {
		return orderCount;
	}


	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}


	public BigDecimal getReceiveableFee() {
		return receiveableFee;
	}


	public void setReceiveableFee(BigDecimal receiveableFee) {
		this.receiveableFee = receiveableFee;
	}


	public BigDecimal getFreightFee() {
		return freightFee;
	}


	public void setFreightFee(BigDecimal freightFee) {
		this.freightFee = freightFee;
	}
}
