package cn.explink.domain;

import java.math.BigDecimal;

public class AccountDeducDetail {
	private long id;
	private long recordid;
	private long branchid;
	private String cwb;
	private long flowordertype;
	private BigDecimal fee;
	private String memo;
	private String createtime;
	private long userid;
	private String createtimesecond;
	private long recordidvirt;

	// 不在数据库字段
	private String branchname;
	private String username;

	public long getRecordidvirt() {
		return recordidvirt;
	}

	public void setRecordidvirt(long recordidvirt) {
		this.recordidvirt = recordidvirt;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatetimesecond() {
		return createtimesecond;
	}

	public void setCreatetimesecond(String createtimesecond) {
		this.createtimesecond = createtimesecond;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
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
