package cn.explink.domain;

import java.math.BigDecimal;

public class ApplyEditDeliverystate {

	private long id; // 主键id
	private long deliverystateid; // 反馈表id
	private long payupid; // 上交款id（站点结算）
	private long opscwbid; // cwb对应的cwbdetail表的主键id
	private String cwb;// 订单
	private int cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	private long nowdeliverystate;// 当前反馈结果
	private BigDecimal nopos;// 当前非POS金额
	private BigDecimal pos;// 当前POS金额
	private long editnowdeliverystate;// 修改后反馈结果
	private BigDecimal editnopos;// 修改后当前非POS金额
	private BigDecimal editpos;// 修改后当前POS金额
	private String editreason;// 修改原因备注
	private String deliverpodtime;// 反馈时间
	private long deliverid;// 小件员id
	private long applyuserid;// 申请人id
	private long applybranchid;// 申请机构id
	private String applytime;// 申请时间
	private long edituserid;// 修改人id
	private String edittime;// 修改时间
	private long issendcustomer;// 是否已反馈电商
	private long isauditpayup;// 是否已交款审核（站点交款审核）
	private long ishandle;// 是否已处理
	private String editdetail;// 修改后的详情（JSON）（封装deliverystate表中的信息）

	private long state;// 是否已向客服申请 1已申请 0，未申请

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverystateid() {
		return deliverystateid;
	}

	public void setDeliverystateid(long deliverystateid) {
		this.deliverystateid = deliverystateid;
	}

	public long getPayupid() {
		return payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getNowdeliverystate() {
		return nowdeliverystate;
	}

	public void setNowdeliverystate(long nowdeliverystate) {
		this.nowdeliverystate = nowdeliverystate;
	}

	public BigDecimal getNopos() {
		return nopos;
	}

	public void setNopos(BigDecimal nopos) {
		this.nopos = nopos;
	}

	public BigDecimal getPos() {
		return pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public long getEditnowdeliverystate() {
		return editnowdeliverystate;
	}

	public void setEditnowdeliverystate(long editnowdeliverystate) {
		this.editnowdeliverystate = editnowdeliverystate;
	}

	public BigDecimal getEditnopos() {
		return editnopos;
	}

	public void setEditnopos(BigDecimal editnopos) {
		this.editnopos = editnopos;
	}

	public BigDecimal getEditpos() {
		return editpos;
	}

	public void setEditpos(BigDecimal editpos) {
		this.editpos = editpos;
	}

	public String getEditreason() {
		return editreason;
	}

	public void setEditreason(String editreason) {
		this.editreason = editreason;
	}

	public String getDeliverpodtime() {
		return deliverpodtime;
	}

	public void setDeliverpodtime(String deliverpodtime) {
		this.deliverpodtime = deliverpodtime;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
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

	public String getApplytime() {
		return applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

	public long getEdituserid() {
		return edituserid;
	}

	public void setEdituserid(long edituserid) {
		this.edituserid = edituserid;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public long getIssendcustomer() {
		return issendcustomer;
	}

	public void setIssendcustomer(long issendcustomer) {
		this.issendcustomer = issendcustomer;
	}

	public long getIsauditpayup() {
		return isauditpayup;
	}

	public void setIsauditpayup(long isauditpayup) {
		this.isauditpayup = isauditpayup;
	}

	public String getEditdetail() {
		return editdetail;
	}

	public void setEditdetail(String editdetail) {
		this.editdetail = editdetail;
	}

	public long getIshandle() {
		return ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

}
