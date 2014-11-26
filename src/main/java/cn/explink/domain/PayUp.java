package cn.explink.domain;

import java.math.BigDecimal;
import java.security.Timestamp;

import cn.explink.enumutil.BranchEnum;

public class PayUp {

	private long id;
	private String credatetime;
	private String upaccountnumber;
	private String upuserrealname;
	private long upbranchid;
	private String toaccountnumber;
	private String touserrealname;
	private BigDecimal amount;
	private BigDecimal amountPos;
	private int upstate;
	private long branchid;
	private long userid;
	private String remark;
	private int type;
	private int way;
	private String auditingremark;
	private String auditinguser;
	private String auditingtime;
	// 加字段 财务审核产生明细的id
	private long auditid;

	private String updateTime; // 修改订单的时间，代表本次交款中的订单最后一次修改时间，任何本次归班中的订单更新都将更新这个时间

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getAmountPos() {
		return amountPos;
	}

	public void setAmountPos(BigDecimal amountPos) {
		this.amountPos = amountPos;
	}

	public long getUpbranchid() {
		return upbranchid;
	}

	public void setUpbranchid(long upbranchid) {
		this.upbranchid = upbranchid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCredatetime() {
		return credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public String getUpaccountnumber() {
		return upaccountnumber;
	}

	public void setUpaccountnumber(String upaccountnumber) {
		this.upaccountnumber = upaccountnumber;
	}

	public String getUpuserrealname() {
		return upuserrealname;
	}

	public void setUpuserrealname(String upuserrealname) {
		this.upuserrealname = upuserrealname;
	}

	public String getToaccountnumber() {
		return toaccountnumber;
	}

	public void setToaccountnumber(String toaccountnumber) {
		this.toaccountnumber = toaccountnumber;
	}

	public String getTouserrealname() {
		return touserrealname;
	}

	public void setTouserrealname(String touserrealname) {
		this.touserrealname = touserrealname;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getUpstate() {
		return upstate;
	}

	public void setUpstate(int upstate) {
		this.upstate = upstate;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuditingremark() {
		return auditingremark;
	}

	public void setAuditingremark(String auditingremark) {
		this.auditingremark = auditingremark;
	}

	public String getAuditinguser() {
		return auditinguser;
	}

	public void setAuditinguser(String auditinguser) {
		this.auditinguser = auditinguser;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public long getAuditid() {
		return auditid;
	}

	public void setAuditid(long auditid) {
		this.auditid = auditid;
	}

}
