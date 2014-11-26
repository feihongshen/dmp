package cn.explink.domain;

import java.math.BigDecimal;

public class CwbApplyTuiHuo {

	private long id; // 主键id
	private String cwb; // 订单号
	private long customerid; // 供货商id
	private long cwbordertypeid; // 订单类型
	private long applytuihuobranchid; // 申请退货站点id
	private String applytime; // 申请时间
	private long applyuserid; // 申请人
	private long applybranchid; // 申请站点id
	private String applytuihuoremark; // 退货申请备注
	private BigDecimal receivablefee; // 应收金额
	private BigDecimal paybackfee; // 应退金额
	private long ishandle; // 是否标记为已处理(0未处理，1已处理）
	private String handletime; // 处理时间
	private long handleuserid; // 处理人
	private String handleremark; // 处理备注
	private long isnow; // 标记为是否为最后一次申请(0不是，1是）

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

	public long getApplytuihuobranchid() {
		return applytuihuobranchid;
	}

	public void setApplytuihuobranchid(long applytuihuobranchid) {
		this.applytuihuobranchid = applytuihuobranchid;
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

	public String getApplytuihuoremark() {
		return applytuihuoremark;
	}

	public void setApplytuihuoremark(String applytuihuoremark) {
		this.applytuihuoremark = applytuihuoremark;
	}

	public String getHandleremark() {
		return handleremark;
	}

	public void setHandleremark(String handleremark) {
		this.handleremark = handleremark;
	}

}
