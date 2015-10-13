package cn.explink.controller;

import java.math.BigDecimal;

public class DeliveryStateDTO {

	private long branchid;
	private BigDecimal receivedfee;

	public BigDecimal getReceivedfee() {
		return receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

}
