package cn.explink.controller;

import java.math.BigDecimal;

public class BranchPayamountSUMDTO {

	private BigDecimal cash; // 其他金额
	private BigDecimal otherfee; // 其他金额
	private BigDecimal pos; // pos实收
	private BigDecimal checkfee; // 支票实收

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getPos() {
		return pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public BigDecimal getCheckfee() {
		return checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

}
