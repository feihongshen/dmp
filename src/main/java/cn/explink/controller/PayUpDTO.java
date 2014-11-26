package cn.explink.controller;

import java.math.BigDecimal;

import net.sf.json.JSONObject;

public class PayUpDTO {

	private String ids;
	private String auditids;
	private String credatetime;
	private BigDecimal amount;
	private BigDecimal amountPos;
	private String upuserrealname;
	private long branchid;
	private String remark;
	private String types;
	private String ways;
	private String auditingremark;
	private String auditinguser;
	private String auditingtime;

	private JSONObject aduitJson;

	private BigDecimal ramount;
	private BigDecimal ramountPos;

	private String updateTime; // 修改订单的时间，代表本次交款中的订单最后一次修改时间，任何本次归班中的订单更新都将更新这个时间

	public String getAuditids() {
		return auditids;
	}

	public void setAuditids(String auditids) {
		this.auditids = auditids;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCredatetime() {
		return credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountPos() {
		return amountPos;
	}

	public void setAmountPos(BigDecimal amountPos) {
		this.amountPos = amountPos;
	}

	public String getUpuserrealname() {
		return upuserrealname;
	}

	public void setUpuserrealname(String upuserrealname) {
		this.upuserrealname = upuserrealname;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getWays() {
		return ways;
	}

	public void setWays(String ways) {
		this.ways = ways;
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

	public JSONObject getAduitJson() {
		return aduitJson;
	}

	public void setAduitJson(JSONObject aduitJson) {
		this.aduitJson = aduitJson;
	}

	public BigDecimal getRamount() {
		return ramount;
	}

	public void setRamount(BigDecimal ramount) {
		this.ramount = ramount;
	}

	public BigDecimal getRamountPos() {
		return ramountPos;
	}

	public void setRamountPos(BigDecimal ramountPos) {
		this.ramountPos = ramountPos;
	}

}
