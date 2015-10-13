package cn.explink.domain;

import java.math.BigDecimal;

public class KuFangZaiTuOrder {
	private long id;
	private String cwb;
	private long cwbordertypeid;
	private long deliverybranchid;
	private String emaildate;
	private String outwarehousetime;
	private long outbranchid;
	private long nextbranchid;
	private long customerid;
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getOutwarehousetime() {
		return outwarehousetime;
	}

	public void setOutwarehousetime(String outwarehousetime) {
		this.outwarehousetime = outwarehousetime;
	}

	public long getOutbranchid() {
		return outbranchid;
	}

	public void setOutbranchid(long outbranchid) {
		this.outbranchid = outbranchid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
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

}
