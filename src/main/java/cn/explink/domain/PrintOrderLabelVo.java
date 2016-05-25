package cn.explink.domain;

/**
 * 面单打印
 * 2016年5月25日 下午3:39:03
 */
public class PrintOrderLabelVo {
	
	private CwbOrder cwbOrder;
	
	private Branch branch;

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

	@Override
	public String toString() {
		return "PrintOrderLabelVo [cwbOrder=" + cwbOrder + ", branch=" + branch + "]";
	}
}
