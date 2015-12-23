package cn.explink.domain;

import java.math.BigDecimal;

public class FeeWayTypeRemark {
	//修改金额时用到字段
	private String cwb;
	private String isDeliveryState;
	private BigDecimal receivablefee;
	private BigDecimal cash;
	private BigDecimal pos;
	private BigDecimal checkfee;
	private BigDecimal otherfee;
	private BigDecimal Paybackfee;
	private BigDecimal shouldfare;
	private long requestUser;

	public long getRequestUser() {
		return this.requestUser;
	}

	public void setRequestUser(long requestUser) {
		this.requestUser = requestUser;
	}

	//修改支付方式时用到字段
	private int paywayid;
	private int newpaywayid;

	//修改订单类型时用到字段
	private int cwbordertypeid;
	private int newcwbordertypeid;

	public int getPaywayid() {
		return this.paywayid;
	}

	public void setPaywayid(int paywayid) {
		this.paywayid = paywayid;
	}

	public int getNewpaywayid() {
		return this.newpaywayid;
	}

	public void setNewpaywayid(int newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public int getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public int getNewcwbordertypeid() {
		return this.newcwbordertypeid;
	}

	public void setNewcwbordertypeid(int newcwbordertypeid) {
		this.newcwbordertypeid = newcwbordertypeid;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getIsDeliveryState() {
		return this.isDeliveryState;
	}

	public void setIsDeliveryState(String isDeliveryState) {
		this.isDeliveryState = isDeliveryState;
	}

	public BigDecimal getReceivablefee() {
		return this.receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getCash() {
		return this.cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getPos() {
		return this.pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public BigDecimal getCheckfee() {
		return this.checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

	public BigDecimal getOtherfee() {
		return this.otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getPaybackfee() {
		return this.Paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.Paybackfee = paybackfee;
	}

	public BigDecimal getShouldfare() {
		return this.shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

}
