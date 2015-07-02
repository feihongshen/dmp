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
	private long state;// 是否已向客服申请 1已申请 0，未申请(已提交，提交状态)
	private long audit;// 有代收款的订单 客服是否已经审核 1已经审核,0,未审核
	//新加字段
	private long cwbstate;//订单状态
	private int shenhestate;//审核状态
	
	// 用于显示时使用
	private String reasoncontent;
	private String editusername;// 处理人
	private String handlename;// 处理状态
	
	private String currentbranchname;//当前站点
	private String applybranchname;//申请站点
	

	private String nowdeliveryname;//当前配送结果
	private String editnowdeliveryname;//修改当前配送结果
	private String delivername;//小件员名字
	
	public String getNowdeliveryname() {
		return nowdeliveryname;
	}
	public void setNowdeliveryname(String nowdeliveryname) {
		this.nowdeliveryname = nowdeliveryname;
	}
	public String getEditnowdeliveryname() {
		return editnowdeliveryname;
	}
	public void setEditnowdeliveryname(String editnowdeliveryname) {
		this.editnowdeliveryname = editnowdeliveryname;
	}
	public String getDelivername() {
		return delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getCurrentbranchname() {
		return currentbranchname;
	}

	public void setCurrentbranchname(String currentbranchname) {
		this.currentbranchname = currentbranchname;
	}

	public String getApplybranchname() {
		return applybranchname;
	}

	public void setApplybranchname(String applybranchname) {
		this.applybranchname = applybranchname;
	}

	public String getEditusername() {
		return editusername;
	}

	public void setEditusername(String editusername) {
		this.editusername = editusername;
	}

	public String getHandlename() {
		return handlename;
	}

	public void setHandlename(String handlename) {
		this.handlename = handlename;
	}

	public String getReasoncontent() {
		return reasoncontent;
	}

	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
	}

	private int reasonid;//
	public int getReasonid() {
		return reasonid;
	}

	public void setReasonid(int reasonid) {
		this.reasonid = reasonid;
	}

	public int getShenhestate() {
		return shenhestate;
	}

	public void setShenhestate(int shenhestate) {
		this.shenhestate = shenhestate;
	}

	public long getCwbstate() {
		return cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverystateid() {
		return this.deliverystateid;
	}

	public void setDeliverystateid(long deliverystateid) {
		this.deliverystateid = deliverystateid;
	}

	public long getPayupid() {
		return this.payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public int getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getNowdeliverystate() {
		return this.nowdeliverystate;
	}

	public void setNowdeliverystate(long nowdeliverystate) {
		this.nowdeliverystate = nowdeliverystate;
	}

	public BigDecimal getNopos() {
		return this.nopos;
	}

	public void setNopos(BigDecimal nopos) {
		this.nopos = nopos;
	}

	public BigDecimal getPos() {
		return this.pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public long getEditnowdeliverystate() {
		return this.editnowdeliverystate;
	}

	public void setEditnowdeliverystate(long editnowdeliverystate) {
		this.editnowdeliverystate = editnowdeliverystate;
	}

	public BigDecimal getEditnopos() {
		return this.editnopos;
	}

	public void setEditnopos(BigDecimal editnopos) {
		this.editnopos = editnopos;
	}

	public BigDecimal getEditpos() {
		return this.editpos;
	}

	public void setEditpos(BigDecimal editpos) {
		this.editpos = editpos;
	}

	public String getEditreason() {
		return this.editreason;
	}

	public void setEditreason(String editreason) {
		this.editreason = editreason;
	}

	public String getDeliverpodtime() {
		return this.deliverpodtime;
	}

	public void setDeliverpodtime(String deliverpodtime) {
		this.deliverpodtime = deliverpodtime;
	}

	public long getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public long getApplyuserid() {
		return this.applyuserid;
	}

	public void setApplyuserid(long applyuserid) {
		this.applyuserid = applyuserid;
	}

	public long getApplybranchid() {
		return this.applybranchid;
	}

	public void setApplybranchid(long applybranchid) {
		this.applybranchid = applybranchid;
	}

	public String getApplytime() {
		return this.applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

	public long getEdituserid() {
		return this.edituserid;
	}

	public void setEdituserid(long edituserid) {
		this.edituserid = edituserid;
	}

	public String getEdittime() {
		return this.edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public long getIssendcustomer() {
		return this.issendcustomer;
	}

	public void setIssendcustomer(long issendcustomer) {
		this.issendcustomer = issendcustomer;
	}

	public long getIsauditpayup() {
		return this.isauditpayup;
	}

	public void setIsauditpayup(long isauditpayup) {
		this.isauditpayup = isauditpayup;
	}

	public String getEditdetail() {
		return this.editdetail;
	}

	public void setEditdetail(String editdetail) {
		this.editdetail = editdetail;
	}

	public long getIshandle() {
		return this.ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getState() {
		return this.state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public long getAudit() {
		return this.audit;
	}

	public void setAudit(long audit) {
		this.audit = audit;
	}

}
