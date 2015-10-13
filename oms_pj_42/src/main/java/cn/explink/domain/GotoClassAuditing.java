package cn.explink.domain;

import java.math.BigDecimal;

public class GotoClassAuditing {

	private int id;
	private String auditingtime;
	private BigDecimal payupamount;
	private int receivedfeeuser;
	private int branchid;
	private int payupid;
	private long classid;
	private String gobackidstr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public BigDecimal getPayupamount() {
		return payupamount;
	}

	public void setPayupamount(BigDecimal payupamount) {
		this.payupamount = payupamount;
	}

	public int getReceivedfeeuser() {
		return receivedfeeuser;
	}

	public void setReceivedfeeuser(int receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public int getPayupid() {
		return payupid;
	}

	public void setPayupid(int payupid) {
		this.payupid = payupid;
	}

	public long getClassid() {
		return classid;
	}

	public void setClassid(long classid) {
		this.classid = classid;
	}

	public String getGobackidstr() {
		return gobackidstr;
	}

	public void setGobackidstr(String gobackidstr) {
		this.gobackidstr = gobackidstr;
	}

}
