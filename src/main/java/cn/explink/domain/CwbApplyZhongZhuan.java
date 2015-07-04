package cn.explink.domain;

import java.math.BigDecimal;

public class CwbApplyZhongZhuan {

	private long id; // 主键id
	private String cwb; // 订单号
	private long customerid; // 供货商id
	private long cwbordertypeid; // 订单类型
	private long applyzhongzhuanbranchid; // 申请中转站点id
	private String applytime; // 申请时间
	private long applyuserid; // 申请人
	private long applybranchid; // 申请站点id
	private String applyzhongzhuanremark; // 中转申请备注
	private BigDecimal receivablefee; // 应收金额
	private BigDecimal paybackfee; // 应退金额
	private long ishandle; // 是否标记为已处理(0未审核，2审核不通过 3审核通过）
	private String handletime; // 处理时间
	private long handleuserid; // 处理人
	private String handleremark; // 处理备注
	private long isnow; // 标记为是否为最后一次申请(0不是，1是）
	private String auditname;//审核人
	private String audittime;//审核时间
	
	public String getAuditname() {
		return auditname;
	}

	public void setAuditname(String auditname) {
		this.auditname = auditname;
	}

	public String getAudittime() {
		return audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	private String arrivebranchtime;
	
	public String getArrivebranchtime() {
		return arrivebranchtime;
	}

	public void setArrivebranchtime(String arrivebranchtime) {
		this.arrivebranchtime = arrivebranchtime;
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

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getApplytime() {
		return applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

	public long getApplyuserid() {
		return applyuserid;
	}

	public void setApplyuserid(long applyuserid) {
		this.applyuserid = applyuserid;
	}

	public long getApplybranchid() {
		return applybranchid;
	}

	public void setApplybranchid(long applybranchid) {
		this.applybranchid = applybranchid;
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

	public long getIshandle() {
		return ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public String getHandletime() {
		return handletime;
	}

	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}

	public long getHandleuserid() {
		return handleuserid;
	}

	public void setHandleuserid(long handleuserid) {
		this.handleuserid = handleuserid;
	}

	public long getIsnow() {
		return isnow;
	}

	public void setIsnow(long isnow) {
		this.isnow = isnow;
	}

	public String getHandleremark() {
		return handleremark;
	}

	public void setHandleremark(String handleremark) {
		this.handleremark = handleremark;
	}

	public long getApplyzhongzhuanbranchid() {
		return applyzhongzhuanbranchid;
	}

	public void setApplyzhongzhuanbranchid(long applyzhongzhuanbranchid) {
		this.applyzhongzhuanbranchid = applyzhongzhuanbranchid;
	}

	public String getApplyzhongzhuanremark() {
		return applyzhongzhuanremark;
	}

	public void setApplyzhongzhuanremark(String applyzhongzhuanremark) {
		this.applyzhongzhuanremark = applyzhongzhuanremark;
	}

}
