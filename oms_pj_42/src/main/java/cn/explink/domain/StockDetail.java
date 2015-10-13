package cn.explink.domain;

public class StockDetail {
	long id;
	String cwb;
	long branchid;
	long type;
	long orderflowid;
	long resultid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public long getOrderflowid() {
		return orderflowid;
	}

	public void setOrderflowid(long orderflowid) {
		this.orderflowid = orderflowid;
	}

	public long getResultid() {
		return resultid;
	}

	public void setResultid(long resultid) {
		this.resultid = resultid;
	}

}
