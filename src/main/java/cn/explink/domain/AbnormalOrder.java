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
	private long flowordertype;
	private long deliverybranchid;
	private String emaildata;
	private String cwb;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getAbnormaltypeid() {
		return this.abnormaltypeid;
	}

	public void setAbnormaltypeid(long abnormaltypeid) {
		this.abnormaltypeid = abnormaltypeid;
	}

	public long getCreuserid() {
		return this.creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCredatetime() {
		return this.credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getIshandle() {
		return this.ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getIsnow() {
		return this.isnow;
	}

	public void setIsnow(long isnow) {
		this.isnow = isnow;
	}

	public long getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getEmaildata() {
		return this.emaildata;
	}

	public void setEmaildata(String emaildata) {
		this.emaildata = emaildata;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
