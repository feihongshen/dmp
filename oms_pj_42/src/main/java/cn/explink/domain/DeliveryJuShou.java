package cn.explink.domain;

import java.math.BigDecimal;

public class DeliveryJuShou {
	private long id;
	private String cwb;
	private String emaildate;
	private long branchid;
	private long deliveryid;
	private long cwbordertypeid;// 订单类型
	private long customerid;
	private long deliverystateid;// 反馈状态 拒收
	private String deliverytime;// 反馈时间
	private String audittime;// 审核时间
	private long gcaid; // 归班状态
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;
	private long deliverystate;

	public long getId() {
		return id;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(long deliveryid) {
		this.deliveryid = deliveryid;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getDeliverystateid() {
		return deliverystateid;
	}

	public void setDeliverystateid(long deliverystateid) {
		this.deliverystateid = deliverystateid;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getAudittime() {
		return audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	public long getGcaid() {
		return gcaid;
	}

	public void setGcaid(long gcaid) {
		this.gcaid = gcaid;
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
