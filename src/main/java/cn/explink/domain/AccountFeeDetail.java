package cn.explink.domain;

import java.math.BigDecimal;

public class AccountFeeDetail {
	private long feedetailid;
	private long feetypeid;
	private long summaryid;
	private long branchid;
	private String detailname;
	private long checkoutstate;
	private BigDecimal customfee;
	private String createtime;
	private long userid;

	// 站点名称 (不属于数据库字段)
	private String branchname;
	private String feetypename;
	private long feetype;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFeetypename() {
		return feetypename;
	}

	public void setFeetypename(String feetypename) {
		this.feetypename = feetypename;
	}

	public long getFeetype() {
		return feetype;
	}

	public void setFeetype(long feetype) {
		this.feetype = feetype;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public long getFeedetailid() {
		return feedetailid;
	}

	public void setFeedetailid(long feedetailid) {
		this.feedetailid = feedetailid;
	}

	public long getFeetypeid() {
		return feetypeid;
	}

	public void setFeetypeid(long feetypeid) {
		this.feetypeid = feetypeid;
	}

	public long getSummaryid() {
		return summaryid;
	}

	public void setSummaryid(long summaryid) {
		this.summaryid = summaryid;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getDetailname() {
		return detailname;
	}

	public void setDetailname(String detailname) {
		this.detailname = detailname;
	}

	public long getCheckoutstate() {
		return checkoutstate;
	}

	public void setCheckoutstate(long checkoutstate) {
		this.checkoutstate = checkoutstate;
	}

	public BigDecimal getCustomfee() {
		return customfee;
	}

	public void setCustomfee(BigDecimal customfee) {
		this.customfee = customfee;
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
