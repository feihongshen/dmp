package cn.explink.domain;

public class AbnormalOrder {
	private long id;
	private long opscwbid;
	private long customerid;
	private long ishandle;
	private String describe;
	private long abnormaltypeid;
	private long creuserid;
	private String credatetime;
	private long branchid;
	private long isnow;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getAbnormaltypeid() {
		return abnormaltypeid;
	}

	public void setAbnormaltypeid(long abnormaltypeid) {
		this.abnormaltypeid = abnormaltypeid;
	}

	public long getCreuserid() {
		return creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCredatetime() {
		return credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getIshandle() {
		return ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getIsnow() {
		return isnow;
	}

	public void setIsnow(long isnow) {
		this.isnow = isnow;
	}

}
