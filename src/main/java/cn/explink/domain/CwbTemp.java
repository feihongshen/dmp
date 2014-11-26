package cn.explink.domain;

public class CwbTemp {
	private long id;
	private String cwb;
	private String createtime;
	private String cwbtype;
	private int state;
	private String asnCwb;
	private String soCwb;

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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCwbtype() {
		return cwbtype;
	}

	public void setCwbtype(String cwbtype) {
		this.cwbtype = cwbtype;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAsnCwb() {
		return asnCwb;
	}

	public void setAsnCwb(String asnCwb) {
		this.asnCwb = asnCwb;
	}

	public String getSoCwb() {
		return soCwb;
	}

	public void setSoCwb(String soCwb) {
		this.soCwb = soCwb;
	}

}
