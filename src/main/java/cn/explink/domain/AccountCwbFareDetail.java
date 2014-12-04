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

	private int verifyflag; // 是否审核 0默认 1已审核
	private String verifytime; // 审核时间r

	private long userid; // 审核时间r

	public int getVerifyflag() {
		return this.verifyflag;
	}

	public void setVerifyflag(int verifyflag) {
		this.verifyflag = verifyflag;
	}

	public String getVerifytime() {
		return this.verifytime;
	}

	public void setVerifytime(String verifytime) {
		this.verifytime = verifytime;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getAudittime() {
		return this.audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	public long getDeliverystate() {
		return this.deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public BigDecimal getShouldfare() {
		return this.shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public BigDecimal getInfactfare() {
		return this.infactfare;
	}

	public void setInfactfare(BigDecimal infactfare) {
		this.infactfare = infactfare;
	}

	public String getPayuptime() {
		return this.payuptime;
	}

	public void setPayuptime(String payuptime) {
		this.payuptime = payuptime;
	}

	public long getFareid() {
		return this.fareid;
	}

	public void setFareid(long fareid) {
		this.fareid = fareid;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

}
