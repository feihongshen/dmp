package cn.explink.domain;

import java.math.BigDecimal;

public class AccountCwbDetail {
	private long accountcwbid;
	private long branchid;
	private long customerid;
	private long flowordertype;
	private int deliverystate;
	private long checkoutstate;
	private long debetstate;
	private String cwb;
	private long cwbordertypeid;
	private long sendcarnum;
	private long scannum;
	private BigDecimal caramount;
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;
	private BigDecimal pos;
	private BigDecimal cash;
	private BigDecimal checkfee;
	private BigDecimal otherfee;
	private String createtime;
	private long userid;
	private long currentbranchid;
	private long accountbranch;

	// 不在数据库的字段
	private String deliverytime;

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public long getAccountcwbid() {
		return accountcwbid;
	}

	public void setAccountcwbid(long accountcwbid) {
		this.accountcwbid = accountcwbid;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public int getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(int deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getCheckoutstate() {
		return checkoutstate;
	}

	public void setCheckoutstate(long checkoutstate) {
		this.checkoutstate = checkoutstate;
	}

	public long getDebetstate() {
		return debetstate;
	}

	public void setDebetstate(long debetstate) {
		this.debetstate = debetstate;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public long getScannum() {
		return scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
	}

	public BigDecimal getCaramount() {
		return caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public BigDecimal getPos() {
		return pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getCurrentbranchid() {
		return currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getAccountbranch() {
		return accountbranch;
	}

	public void setAccountbranch(long accountbranch) {
		this.accountbranch = accountbranch;
	}

}
