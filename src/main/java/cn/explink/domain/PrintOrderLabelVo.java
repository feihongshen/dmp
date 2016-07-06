package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 面单打印
 * 2016年5月25日 下午3:39:03
 */
public class PrintOrderLabelVo {
	
	/**
	 *  订单主表
	 */
	private CwbOrder cwbOrder;
	
	/**
	 * 站点
	 */
	private Branch branch;
	
	/**
	 * 应收金额
	 */
	private BigDecimal shouldReceiveTotal;
	
	/**
	 * 目的地
	 */
	private String destination;

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public BigDecimal getShouldReceiveTotal() {
		return shouldReceiveTotal;
	}

	public void setShouldReceiveTotal(BigDecimal shouldReceiveTotal) {
		this.shouldReceiveTotal = shouldReceiveTotal;
	}
}
