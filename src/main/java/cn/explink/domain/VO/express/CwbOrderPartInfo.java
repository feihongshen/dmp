package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 订单的部分信息用于核对
 * @author jiangyu 2015年8月21日
 *
 */
public class CwbOrderPartInfo {
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 运费合计
	 */
	private BigDecimal deliveryFee;
	/**
	 * 站点id
	 */
	private Long branchId;
	
	public CwbOrderPartInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
}
