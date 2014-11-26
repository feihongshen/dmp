package cn.explink.domain;

public class BackDetail {
	private long id;
	private long branchid;
	private String cwb;
	private long time24;
	private long time72;
	private int type;
	private int intoflag;
	private String createtime;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

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

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getTime24() {
		return time24;
	}

	public void setTime24(long time24) {
		this.time24 = time24;
	}

	public long getTime72() {
		return time72;
	}

	public void setTime72(long time72) {
		this.time72 = time72;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIntoflag() {
		return intoflag;
	}

	public void setIntoflag(int intoflag) {
		this.intoflag = intoflag;
	}

}
