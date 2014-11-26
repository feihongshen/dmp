package cn.explink.domain;

import java.math.BigDecimal;

public class FinanceAudit {
	private long id;
	private long customerid;
	private long customerwarehouseid;
	private long cwbcount;
	private String paydatetime;
	private int paytype;
	private BigDecimal shouldpayamount;
	private BigDecimal payamount;
	private String paynumber;
	private String auditdatetime;
	private String payremark;
	private int type;
	private BigDecimal qiankuanamount;
	private BigDecimal payqiankuanamount;
	private String payqiankuandatetime;
	private int cwbordertype;
	private String updateTime;

	public FinanceAudit() {
	}

	public FinanceAudit(long id, long customerid, long customerwarehouseid, long cwbcount, String paydatetime, int paytype, BigDecimal shouldpayamount, BigDecimal payamount, String paynumber,
			String auditdatetime, String payremark2, int type, BigDecimal qiankuanamount, BigDecimal payqiankuanamount, String payqiankuandatetime, int cwbordertype) {
		this.id = id;
		this.customerid = customerid;
		this.customerwarehouseid = customerwarehouseid;
		this.cwbcount = cwbcount;
		this.paydatetime = paydatetime;
		this.paytype = paytype;
		this.shouldpayamount = shouldpayamount;
		this.payamount = payamount;
		this.paynumber = paynumber;
		this.auditdatetime = auditdatetime;
		this.payremark = payremark2;
		this.type = type;
		this.qiankuanamount = qiankuanamount;
		this.payqiankuanamount = payqiankuanamount;
		this.payqiankuandatetime = payqiankuandatetime;
		this.cwbordertype = cwbordertype;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getCwbordertype() {
		return cwbordertype;
	}

	public void setCwbordertype(int cwbordertype) {
		this.cwbordertype = cwbordertype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getCustomerwarehouseid() {
		return customerwarehouseid;
	}

	public void setCustomerwarehouseid(long customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
	}

	public long getCwbcount() {
		return cwbcount;
	}

	public void setCwbcount(long cwbcount) {
		this.cwbcount = cwbcount;
	}

	public String getPaydatetime() {
		return paydatetime;
	}

	public void setPaydatetime(String paydatetime) {
		this.paydatetime = paydatetime;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public BigDecimal getShouldpayamount() {
		return shouldpayamount;
	}

	public void setShouldpayamount(BigDecimal shouldpayamount) {
		this.shouldpayamount = shouldpayamount;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public String getPaynumber() {
		return paynumber;
	}

	public void setPaynumber(String paynumber) {
		this.paynumber = paynumber;
	}

	public String getAuditdatetime() {
		return auditdatetime;
	}

	public void setAuditdatetime(String auditdatetime) {
		this.auditdatetime = auditdatetime;
	}

	public String getPayremark() {
		return payremark;
	}

	public void setPayremark(String payremark) {
		this.payremark = payremark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getQiankuanamount() {
		return qiankuanamount;
	}

	public void setQiankuanamount(BigDecimal qiankuanamount) {
		this.qiankuanamount = qiankuanamount;
	}

	public BigDecimal getPayqiankuanamount() {
		return payqiankuanamount;
	}

	public void setPayqiankuanamount(BigDecimal payqiankuanamount) {
		this.payqiankuanamount = payqiankuanamount;
	}

	public String getPayqiankuandatetime() {
		return payqiankuandatetime;
	}

	public void setPayqiankuandatetime(String payqiankuandatetime) {
		this.payqiankuandatetime = payqiankuandatetime;
	}

	public String toString() {
		return "FinanceAudit [id=" + id + ", customerid=" + customerid + ", customerwarehouseid=" + customerwarehouseid + ", cwbcount=" + cwbcount + ", paydatetime=" + paydatetime + ", paytype="
				+ paytype + ", shouldpayamount=" + shouldpayamount + ", payamount=" + payamount + ", paynumber=" + paynumber + ", auditdatetime=" + auditdatetime + ", payremark=" + payremark
				+ ", type=" + type + ", qiankuanamount=" + qiankuanamount + ", payqiankuanamount=" + payqiankuanamount + ", payqiankuandatetime=" + payqiankuandatetime + ", cwbordertype="
				+ cwbordertype + "]";
	}

}
