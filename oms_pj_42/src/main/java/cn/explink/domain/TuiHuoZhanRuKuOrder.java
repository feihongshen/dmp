package cn.explink.domain;

import java.math.BigDecimal;

public class TuiHuoZhanRuKuOrder {

	private long id;
	private String cwb;
	private long cwbordertypeid;
	private long tuihuobranchid;
	private long deliverystateid;
	private String rukutime;
	private long customerid;
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;

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

	public long getTuihuobranchid() {
		return tuihuobranchid;
	}

	public void setTuihuobranchid(long tuihuobranchid) {
		this.tuihuobranchid = tuihuobranchid;
	}

	public long getDeliverystateid() {
		return deliverystateid;
	}

	public void setDeliverystateid(long deliverystateid) {
		this.deliverystateid = deliverystateid;
	}

	public String getRukutime() {
		return rukutime;
	}

	public void setRukutime(String rukutime) {
		this.rukutime = rukutime;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
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
