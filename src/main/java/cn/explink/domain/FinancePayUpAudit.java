package cn.explink.domain;

import java.math.BigDecimal;

public class FinancePayUpAudit {
	private long id;
	private String credate;
	private long userid;
	private BigDecimal amount;
	private BigDecimal amountpos;
	private BigDecimal payamount;
	private BigDecimal payamountpos;
	private String auditpayupid;
	private String updateTime;

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountpos() {
		return amountpos;
	}

	public void setAmountpos(BigDecimal amountpos) {
		this.amountpos = amountpos;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public BigDecimal getPayamountpos() {
		return payamountpos;
	}

	public void setPayamountpos(BigDecimal payamountpos) {
		this.payamountpos = payamountpos;
	}

	public String getAuditpayupid() {
		return auditpayupid;
	}

	public void setAuditpayupid(String auditpayupid) {
		this.auditpayupid = auditpayupid;
	}

	public String toString() {
		return "FinancePayUpAudit [id=" + id + ", credate=" + credate + ", userid=" + userid + ", amount=" + amount + ", amountpos=" + amountpos + ", payamount=" + payamount + ", payamountpos="
				+ payamountpos + ", auditpayupid=" + auditpayupid + "]";
	}

}
