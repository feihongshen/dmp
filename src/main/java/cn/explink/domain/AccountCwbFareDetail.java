package cn.explink.domain;

import java.math.BigDecimal;

public class AccountCwbFareDetail {
	private long id;
	private String cwb;
	private long customerid;
	private long cwbordertypeid;
	private long deliverybranchid;
	private String audittime;// 归班审核时间
	private long deliverystate;
	private BigDecimal shouldfare;// 应收运费
	private BigDecimal infactfare;// 实收运费
	private String payuptime;// 交款时间
	private long fareid;// 对应交款表id

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

	public String getAudittime() {
		return audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public BigDecimal getShouldfare() {
		return shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public BigDecimal getInfactfare() {
		return infactfare;
	}

	public void setInfactfare(BigDecimal infactfare) {
		this.infactfare = infactfare;
	}

	public String getPayuptime() {
		return payuptime;
	}

	public void setPayuptime(String payuptime) {
		this.payuptime = payuptime;
	}

	public long getFareid() {
		return fareid;
	}

	public void setFareid(long fareid) {
		this.fareid = fareid;
	}

}
