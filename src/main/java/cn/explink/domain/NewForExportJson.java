package cn.explink.domain;

import java.math.BigDecimal;

import net.sf.json.JSONObject;

public class NewForExportJson {
	private long id;
	private String countCwb;

	private String sumCash;// 总现金

	private String sumPos;// 包含pos
	private String sumCheckfee;// 其他
	private long branchid;// 站点id
	private String sumOrderfee;// 支票失手
	private String sumReturnfee;// 总的钱数
	private JSONObject aduitJson;
	private BigDecimal ramount;
	private BigDecimal ramountPos;
	private String credatetime;// 开始时间
	private String auditingtime;// 员工交期日期
	private BigDecimal amount;// 当日应上交
	private BigDecimal amountPos;// 不含pos应上交

	public String getCredatetime() {
		return credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
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

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
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

	public JSONObject getAduitJson() {
		return aduitJson;
	}

	public void setAduitJson(JSONObject aduitJson) {
		this.aduitJson = aduitJson;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountCwb() {
		return countCwb;
	}

	public void setCountCwb(String countCwb) {
		this.countCwb = countCwb;
	}

	public String getSumCash() {
		return sumCash;
	}

	public void setSumCash(String sumCash) {
		this.sumCash = sumCash;
	}

	public String getSumPos() {
		return sumPos;
	}

	public void setSumPos(String sumPos) {
		this.sumPos = sumPos;
	}

	public String getSumCheckfee() {
		return sumCheckfee;
	}

	public void setSumCheckfee(String sumCheckfee) {
		this.sumCheckfee = sumCheckfee;
	}

	public String getSumOrderfee() {
		return sumOrderfee;
	}

	public void setSumOrderfee(String sumOrderfee) {
		this.sumOrderfee = sumOrderfee;
	}

	public String getSumReturnfee() {
		return sumReturnfee;
	}

	public void setSumReturnfee(String sumReturnfee) {
		this.sumReturnfee = sumReturnfee;
	}

}
