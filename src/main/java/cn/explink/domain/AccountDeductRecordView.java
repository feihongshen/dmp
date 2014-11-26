package cn.explink.domain;

import java.math.BigDecimal;

public class AccountDeductRecordView {
	private String recordtype;
	private BigDecimal fee;
	private BigDecimal beforefee;
	private BigDecimal afterfee;
	private String memo;
	private String createtime;
	private String username;
	private BigDecimal beforedebt;
	private BigDecimal afterdebt;
	private String cwb;
	private long nums;

	public long getNums() {
		return nums;
	}

	public void setNums(long nums) {
		this.nums = nums;
	}

	public String getRecordtype() {
		return recordtype;
	}

	public void setRecordtype(String recordtype) {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
