package cn.explink.controller;

import java.math.BigDecimal;

public class MonitorDTO {
	private long branchid;
	private long countsum;// 订单数量
	private BigDecimal caramountsum = BigDecimal.ZERO;// 金额总量
	private String cwbStr;// 订单号字符串
	private BigDecimal caramountpos = BigDecimal.ZERO;// pos总和
	private BigDecimal paybackfee = BigDecimal.ZERO;// 应退金额总和
	private long tuihuoid;// 退货id
	private long zhongzhuanid;// 中转id

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getCountsum() {
		return countsum;
	}

	public void setCountsum(long countsum) {
		this.countsum = countsum;
	}

	public BigDecimal getCaramountsum() {
		if (caramountsum == null) {
			caramountsum = new BigDecimal("0.00");
		}
		return caramountsum;
	}

	public void setCaramountsum(BigDecimal caramountsum) {
		this.caramountsum = caramountsum;
	}

	public String getCwbStr() {
		return cwbStr;
	}

	public void setCwbStr(String cwbStr) {
		this.cwbStr = cwbStr;
	}

	public BigDecimal getCaramountpos() {
		if (caramountpos == null) {
			caramountpos = new BigDecimal("0.00");
		}
		return caramountpos;
	}

	public void setCaramountpos(BigDecimal caramountpos) {
		this.caramountpos = caramountpos;
	}

	public BigDecimal getPaybackfee() {
		if (paybackfee == null) {
			paybackfee = new BigDecimal("0.00");
		}
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public long getTuihuoid() {
		return tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	public long getZhongzhuanid() {
		return zhongzhuanid;
	}

	public void setZhongzhuanid(long zhongzhuanid) {
		this.zhongzhuanid = zhongzhuanid;
	}

}
