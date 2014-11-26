package cn.explink.domain;

public class AccountFeeType {
	private long feetypeid;
	private String feetypename;
	private long feetype;
	private String createtime;
	private long userid;
	private long effectflag;

	public long getEffectflag() {
		return effectflag;
	}

	public void setEffectflag(long effectflag) {
		this.effectflag = effectflag;
	}

	public long getFeetype() {
		return feetype;
	}

	public void setFeetype(long feetype) {
		this.feetype = feetype;
	}

	public long getFeetypeid() {
		return feetypeid;
	}

	public void setFeetypeid(long feetypeid) {
		this.feetypeid = feetypeid;
	}

	public String getFeetypename() {
		return feetypename;
	}

	public void setFeetypename(String feetypename) {
		this.feetypename = feetypename;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

}
