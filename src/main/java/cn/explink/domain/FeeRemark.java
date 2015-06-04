package cn.explink.domain;

import java.math.BigDecimal;

public class FeeRemark {
	private String cwb;
	private String isDeliveryState;
	private BigDecimal receivablefee;
	private BigDecimal cash;
	private BigDecimal pos;
	private BigDecimal checkfee;
	private BigDecimal otherfee;
	private BigDecimal Paybackfee;
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getIsDeliveryState() {
		return isDeliveryState;
	}
	public void setIsDeliveryState(String isDeliveryState) {
		this.isDeliveryState = isDeliveryState;
	}
	public BigDecimal getReceivablefee() {
		return receivablefee;
	}
	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
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
	public BigDecimal getOtherfee() {
		return otherfee;
	}
	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}
	public BigDecimal getPaybackfee() {
		return Paybackfee;
	}
	public void setPaybackfee(BigDecimal paybackfee) {
		Paybackfee = paybackfee;
	}
	
}
