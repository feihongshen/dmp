package cn.explink.domain;

import java.sql.Timestamp;

public class StockResult {
	long id;
	long branchid;
	Timestamp createtime;
	long state;
	long checkcount;
	long realcount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public long getCheckcount() {
		return checkcount;
	}

	public void setCheckcount(long checkcount) {
		this.checkcount = checkcount;
	}

	public long getRealcount() {
		return realcount;
	}

	public void setRealcount(long realcount) {
		this.realcount = realcount;
	}

	public StockResult() {
	}

	public StockResult(long branchid, long state, long realcount) {
		super();
		this.branchid = branchid;
		this.state = state;
		this.realcount = realcount;
	}

}
