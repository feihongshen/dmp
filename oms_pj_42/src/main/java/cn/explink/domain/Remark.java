package cn.explink.domain;

public class Remark {
	private long remarkid;
	private String remarktype;
	private String remark;
	private String cwb;
	private String createtime;// 创建时间
	private String username;// 创建人

	public long getRemarkid() {
		return remarkid;
	}

	public void setRemarkid(long remarkid) {
		this.remarkid = remarkid;
	}

	public String getRemarktype() {
		return remarktype;
	}

	public void setRemarktype(String remarktype) {
		this.remarktype = remarktype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Remark() {
	}

	public Remark(String remarktype, String remark, String cwb, String createtime, String username) {
		super();
		this.remarktype = remarktype;
		this.remark = remark;
		this.cwb = cwb;
		this.createtime = createtime;
		this.username = username;
	}
}
