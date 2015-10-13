package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 妥投订单汇总表
 * 
 * @author Administrator
 *
 */
public class DeliverySuccessful {
	private long id;
	private String cwb;
	private String createtime;
	private long branchid;
	private long deliveryid;
	private long deliverystate;
	private String cwbordertypeid;
	private long customerid;
	private long deliverystateid;
	private long paywayid;
	private String deliverytime;
	private String audittime;
	private long auditstate;
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额

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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
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

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
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

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
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

	public long getAuditstate() {
		return auditstate;
	}

	public void setAuditstate(long auditstate) {
		this.auditstate = auditstate;
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
