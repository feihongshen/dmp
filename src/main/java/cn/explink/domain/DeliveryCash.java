package cn.explink.domain;

import java.math.BigDecimal;

public class DeliveryCash {
	private long id;
	private long deliverybranchid;// 配送站点
	private long deliverid;
	private String linghuotime;// 领货时间
	private String fankuitime;// 反馈时间
	private String guibantime;// 归班时间
	private long deliverystate;
	private String cwb;
	private long customerid;// 供货商id
	private BigDecimal receivablePosfee = BigDecimal.ZERO;
	private BigDecimal receivableNoPosfee = BigDecimal.ZERO;
	private BigDecimal paybackfee = BigDecimal.ZERO;
	private long deliverystateid;// 反馈表id
	private long gcaid;// 归班id
	private long state;// 状态1为正常，0为失效，默认为1

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getGcaid() {
		return gcaid;
	}

	public void setGcaid(long gcaid) {
		this.gcaid = gcaid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getLinghuotime() {
		return linghuotime;
	}

	public void setLinghuotime(String linghuotime) {
		this.linghuotime = linghuotime;
	}

	public String getFankuitime() {
		return fankuitime;
	}

	public void setFankuitime(String fankuitime) {
		this.fankuitime = fankuitime;
	}

	public String getGuibantime() {
		return guibantime;
	}

	public void setGuibantime(String guibantime) {
		this.guibantime = guibantime;
	}

	public BigDecimal getReceivablePosfee() {
		return receivablePosfee;
	}

	public void setReceivablePosfee(BigDecimal receivablePosfee) {
		this.receivablePosfee = receivablePosfee;
	}

	public BigDecimal getReceivableNoPosfee() {
		return receivableNoPosfee;
	}

	public void setReceivableNoPosfee(BigDecimal receivableNoPosfee) {
		this.receivableNoPosfee = receivableNoPosfee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public long getDeliverystateid() {
		return deliverystateid;
	}

	public void setDeliverystateid(long deliverystateid) {
		this.deliverystateid = deliverystateid;
	}

}
