package cn.explink.domain;

import java.math.BigDecimal;

public class AccountCwbFare {
	private long id;
	private BigDecimal girofee;// 转账金额
	private String girouser;// 转账付款人
	private String girocardno;// 转账卡号
	private BigDecimal cashfee;// 现金金额
	private String cashuser;// 现金付款人
	private String payuptime;// 交款时间
	private long userid;// 操作人id
	private String payremark;// 付款备注
	private String audittime;// 审核时间
	private long audituserid;// 审核人
	private String auditremark;// 审核备注

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getGirofee() {
		return girofee;
	}

	public void setGirofee(BigDecimal girofee) {
		this.girofee = girofee;
	}

	public String getGirocardno() {
		return girocardno;
	}

	public void setGirocardno(String girocardno) {
		this.girocardno = girocardno;
	}

	public BigDecimal getCashfee() {
		return cashfee;
	}

	public void setCashfee(BigDecimal cashfee) {
		this.cashfee = cashfee;
	}

	public String getPayuptime() {
		return payuptime;
	}

	public void setPayuptime(String payuptime) {
		this.payuptime = payuptime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getPayremark() {
		return payremark;
	}

	public void setPayremark(String payremark) {
		this.payremark = payremark;
	}

	public String getAudittime() {
		return audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	public long getAudituserid() {
		return audituserid;
	}

	public void setAudituserid(long audituserid) {
		this.audituserid = audituserid;
	}

	public String getAuditremark() {
		return auditremark;
	}

	public void setAuditremark(String auditremark) {
		this.auditremark = auditremark;
	}

	public String getGirouser() {
		return girouser;
	}

	public void setGirouser(String girouser) {
		this.girouser = girouser;
	}

	public String getCashuser() {
		return cashuser;
	}

	public void setCashuser(String cashuser) {
		this.cashuser = cashuser;
	}

}
