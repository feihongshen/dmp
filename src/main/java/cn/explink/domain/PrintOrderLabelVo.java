package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 面单打印
 * 2016年5月25日 下午3:39:03
 */
public class PrintOrderLabelVo {
	
	private CwbOrder cwbOrder;
	
	private Branch branch;
	
	private BigDecimal shouldReceiveTotal;


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
