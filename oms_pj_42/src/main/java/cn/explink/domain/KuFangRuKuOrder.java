package cn.explink.domain;

import java.math.BigDecimal;

public class KuFangRuKuOrder {
	private long id;
	private String cwb;
	private long customerid;
	private String intowarehousetime;
	private long intobranchid;
	private long cwbordertypeid;
	private long deliverybranchid;
	private long isruku;// 默认0 0未入库 1 已入库
	private String emaildate;
	private BigDecimal paybackfee;
	private BigDecimal receivablefee;
	private long intowarehouseuserid;

	public long getIntowarehouseuserid() {
		return intowarehouseuserid;
	}

	public void setIntowarehouseuserid(long intowarehouseuserid) {
		this.intowarehouseuserid = intowarehouseuserid;
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

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getIntowarehousetime() {
		return intowarehousetime;
	}

	public void setIntowarehousetime(String intowarehousetime) {
		this.intowarehousetime = intowarehousetime;
	}

	public long getIntobranchid() {
		return intobranchid;
	}

	public void setIntobranchid(long intobranchid) {
		this.intobranchid = intobranchid;
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

	public long getIsruku() {
		return isruku;
	}

	public void setIsruku(long isruku) {
		this.isruku = isruku;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

}
