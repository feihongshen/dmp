package cn.explink.domain;

public class Exportwarhousesummary {
	private String cwbs;
	private int branchId;
	private int branchSum;
	private String credate;

	public int getBranchId() {
		return branchId;
	}

	public String getCwbs() {
		return cwbs;
	}

	public void setCwbs(String cwbs) {
		this.cwbs = cwbs;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getBranchSum() {
		return branchSum;
	}

	public void setBranchSum(int branchSum) {
		this.branchSum = branchSum;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

}
