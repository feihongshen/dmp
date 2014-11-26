package cn.explink.domain;

import java.math.BigDecimal;

public class AccountDeductRecord {
	private long recordid;
	private long branchid;
	private long recordtype;
	private BigDecimal fee;
	private BigDecimal beforefee;
	private BigDecimal afterfee;
	private String memo;
	private String createtime;
	private long userid;
	private BigDecimal beforedebt;
	private BigDecimal afterdebt;
	private String cwb;

	// 不在数据库字段
	private long nums;
	private String username;

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getNums() {
		return nums;
	}

	public void setNums(long nums) {
		this.nums = nums;
	}

	public BigDecimal getBeforedebt() {
		return beforedebt;
	}

	public void setBeforedebt(BigDecimal beforedebt) {
		this.beforedebt = beforedebt;
	}

	public BigDecimal getAfterdebt() {
		return afterdebt;
	}

	public void setAfterdebt(BigDecimal afterdebt) {
		this.afterdebt = afterdebt;
	}

	public long getRecordid() {
		return recordid;
	}

	public void setRecordid(long recordid) {
		this.recordid = recordid;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getRecordtype() {
		return recordtype;
	}

	public void setRecordtype(long recordtype) {
		this.recordtype = recordtype;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getBeforefee() {
		return beforefee;
	}

	public void setBeforefee(BigDecimal beforefee) {
		this.beforefee = beforefee;
	}

	public BigDecimal getAfterfee() {
		return afterfee;
	}

	public void setAfterfee(BigDecimal afterfee) {
		this.afterfee = afterfee;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
