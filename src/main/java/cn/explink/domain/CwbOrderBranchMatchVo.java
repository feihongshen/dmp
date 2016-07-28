package cn.explink.domain;

import java.util.List;

/**
 * 订单匹配站点VO
 * 2016年6月15日 下午6:14:51
 */
public class CwbOrderBranchMatchVo {
	/**
	 * 订单
	 */
	private CwbOrder cwbOrder;
	
	/**
	 * 订单类型
	 */
	private String flowordertypeVal;
	
	/**
	 * 订单状态
	 */
	private String orderTypeVal;
	
	/**
	 * 小件员
	 */
	private List<User> courierList;

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public String getFlowordertypeVal() {
		return flowordertypeVal;
	}

	public void setFlowordertypeVal(String flowordertypeVal) {
		this.flowordertypeVal = flowordertypeVal;
	}

	public String getOrderTypeVal() {
		return orderTypeVal;
	}

	public void setOrderTypeVal(String orderTypeVal) {
		this.orderTypeVal = orderTypeVal;
	}

	public List<User> getCourierList() {
		return courierList;
	}

	public void setCourierList(List<User> courierList) {
		this.courierList = courierList;
	}

	@Override
	public String toString() {
		return "CwbOrderBranchMatchVo [cwbOrder=" + cwbOrder + ", flowordertypeVal=" + flowordertypeVal
				+ ", orderTypeVal=" + orderTypeVal + ", courierList=" + courierList + "]";
	}
}
