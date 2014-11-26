package cn.explink.domain;

public class StockDetail {
	long id;
	String cwb;
	long branchid;
	long userid;
	long type;
	long orderflowid;
	long resultid;
	long stocktype;
	String state;
	String transcwb;

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

	public long getStocktype() {
		return stocktype;
	}

	public void setStocktype(long stocktype) {
		this.stocktype = stocktype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

}
