package cn.explink.domain;

public class BranchHisteryLog {
	private long id = 0;
	private long branchlogid = 0;// 站点日志id
	private long logtype = 0;// 类型
	private String cwb = "";// 保存的订单号

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBranchlogid() {
		return branchlogid;
	}

	public void setBranchlogid(long branchlogid) {
		this.branchlogid = branchlogid;
	}

	public long getLogtype() {
		return logtype;
	}

	public void setLogtype(long logtype) {
		this.logtype = logtype;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
