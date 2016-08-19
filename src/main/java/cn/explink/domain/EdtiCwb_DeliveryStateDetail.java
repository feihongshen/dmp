package cn.explink.domain;

import java.math.BigDecimal;

public class EdtiCwb_DeliveryStateDetail {
	private long id;// 自增长id
	private long dsid; // 反馈表id
	private long fd_payup_detail_id; // 小件员交款审核表finance_deliver_pay_up_detail的
										// id
	private long finance_audit_id;// 与供货商结算审核表finance_audit 的id
	private long f_payup_audit_id; // 站点交款审核表finance_pay_up_audit 的id
	private DeliveryState ds;// 反馈表对象

	private BigDecimal new_receivedfee = BigDecimal.ZERO; // 新的收到总金额
	private BigDecimal new_returnedfee = BigDecimal.ZERO; // 新的退还金额
	private BigDecimal new_businessfee = BigDecimal.ZERO; // 新的应处理金额
	private BigDecimal new_shouldfare = BigDecimal.ZERO; // 新的收到总金额
	private long new_isout; // 新的应处理金额类型0应收1应退
	private long new_deliverystate; // 新的订单配送状态 0未反馈
	private BigDecimal new_pos = BigDecimal.ZERO; // 新的POS实收
	private int cwbordertypeid; // 订单类型
	private int new_cwbordertypeid; // 新的订单类型
	private long new_flowordertype; // 新的环节

	private long editcwbtypeid; // 修改订单的类型
	private long requestUser;// 申请人
	private long editUser;// 修改人

	private BigDecimal oriInfactfare;// 原实收运费
	
	/**
	 * 支付方式  V4.2.16资金归集代扣。POSCOD自动回款，如果订单支付类型为POS 或者 COD时，当订单被重置反馈时，需写入一条回款调整记录到调整表 
	 * by gordon.zhou 2016/6/24
	 */
	private int newpaywayid; 

	public long getRequestUser() {
		return this.requestUser;
	}

	public void setRequestUser(long requestUser) {
		this.requestUser = requestUser;
	}

	public long getEditUser() {
		return this.editUser;
	}

	public void setEditUser(long editUser) {
		this.editUser = editUser;
	}

	public long getEditcwbtypeid() {
		return this.editcwbtypeid;
	}

	public void setEditcwbtypeid(long editcwbtypeid) {
		this.editcwbtypeid = editcwbtypeid;
	}

	public int getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDsid() {
		return this.dsid;
	}

	public void setDsid(long dsid) {
		this.dsid = dsid;
	}

	public long getFd_payup_detail_id() {
		return this.fd_payup_detail_id;
	}

	public void setFd_payup_detail_id(long fd_payup_detail_id) {
		this.fd_payup_detail_id = fd_payup_detail_id;
	}

	public long getFinance_audit_id() {
		return this.finance_audit_id;
	}

	public void setFinance_audit_id(long finance_audit_id) {
		this.finance_audit_id = finance_audit_id;
	}

	public long getF_payup_audit_id() {
		return this.f_payup_audit_id;
	}

	public void setF_payup_audit_id(long f_payup_audit_id) {
		this.f_payup_audit_id = f_payup_audit_id;
	}

	public DeliveryState getDs() {
		return this.ds;
	}

	public BigDecimal getNew_receivedfee() {
		return this.new_receivedfee;
	}

	public void setNew_receivedfee(BigDecimal new_receivedfee) {
		this.new_receivedfee = new_receivedfee;
	}

	public BigDecimal getNew_returnedfee() {
		return this.new_returnedfee;
	}

	public void setNew_returnedfee(BigDecimal new_returnedfee) {
		this.new_returnedfee = new_returnedfee;
	}

	public BigDecimal getNew_businessfee() {
		return this.new_businessfee;
	}

	public void setNew_businessfee(BigDecimal new_businessfee) {
		this.new_businessfee = new_businessfee;
	}

	public long getNew_isout() {
		return this.new_isout;
	}

	public void setNew_isout(long new_isout) {
		this.new_isout = new_isout;
	}

	public long getNew_deliverystate() {
		return this.new_deliverystate;
	}

	public void setNew_deliverystate(long new_deliverystate) {
		this.new_deliverystate = new_deliverystate;
	}

	public BigDecimal getNew_pos() {
		return this.new_pos;
	}

	public void setNew_pos(BigDecimal new_pos) {
		this.new_pos = new_pos;
	}

	public int getNew_cwbordertypeid() {
		return this.new_cwbordertypeid;
	}

	public void setNew_cwbordertypeid(int new_cwbordertypeid) {
		this.new_cwbordertypeid = new_cwbordertypeid;
	}

	public long getNew_flowordertype() {
		return this.new_flowordertype;
	}

	public void setNew_flowordertype(long new_flowordertype) {
		this.new_flowordertype = new_flowordertype;
	}

	public BigDecimal getNew_shouldfare() {
		return this.new_shouldfare;
	}

	public void setNew_shouldfare(BigDecimal new_shouldfare) {
		this.new_shouldfare = new_shouldfare;
	}

	public BigDecimal getOriInfactfare() {
		return this.oriInfactfare;
	}

	public void setOriInfactfare(BigDecimal oriInfactfare) {
		this.oriInfactfare = oriInfactfare;
	}

	public void setDs(DeliveryState ds) {
		DeliveryState myDs = new DeliveryState();
		this.dsid = ds.getId();
		myDs.setCwb(ds.getCwb());
		myDs.setDeliveryid(ds.getDeliveryid());
		myDs.setReceivedfee(ds.getReceivedfee());
		myDs.setReturnedfee(ds.getReturnedfee());
		myDs.setBusinessfee(ds.getBusinessfee());
		myDs.setDeliverystate(ds.getDeliverystate());
		myDs.setCash(ds.getCash());
		myDs.setPos(ds.getPos());
		myDs.setPosremark(ds.getPosremark());
		myDs.setMobilepodtime(ds.getMobilepodtime());
		myDs.setCheckfee(ds.getCheckfee());
		myDs.setCheckremark(ds.getCheckremark());
		myDs.setReceivedfeeuser(ds.getReceivedfeeuser());
		myDs.setCreatetime(ds.getCreatetime());
		myDs.setOtherfee(ds.getOtherfee());
		myDs.setPodremarkid(ds.getPodremarkid());
		myDs.setDeliverstateremark(ds.getDeliverstateremark());
		myDs.setIsout(ds.getIsout());
		myDs.setPos_feedback_flag(ds.getPos_feedback_flag());
		myDs.setUserid(ds.getUserid());
		myDs.setGcaid(ds.getGcaid());

		myDs.setSign_typeid(ds.getSign_typeid());
		myDs.setSign_man(ds.getSign_man());
		myDs.setSign_time(ds.getSign_time());
		myDs.setDeliverybranchid(ds.getDeliverybranchid());
		myDs.setCustomerid(ds.getCustomerid());
		myDs.setIssendcustomer(ds.getIssendcustomer());
		myDs.setIsautolinghuo(ds.getIsautolinghuo());
		myDs.setPushtime(ds.getPushtime());

		myDs.setDeliverytime(ds.getDeliverytime());
		myDs.setAuditingtime(ds.getAuditingtime());
		myDs.setPayupid(ds.getPayupid());
		this.ds = myDs;
	}
	
	

	public int getNewpaywayid() {
		return newpaywayid;
	}

	public void setNewpaywayid(int newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	@Override
	public String toString() {
		return "EdtiCwb_DeliveryStateDetail [id=" + this.id + ", dsid=" + this.dsid + ", fd_payup_detail_id=" + this.fd_payup_detail_id + ", finance_audit_id=" + this.finance_audit_id
				+ ", f_payup_audit_id=" + this.f_payup_audit_id + ", ds=" + (this.ds == null ? "null" : this.ds.toString()) + ", new_receivedfee=" + this.new_receivedfee + ", new_returnedfee="
				+ this.new_returnedfee + ", new_businessfee=" + this.new_businessfee + ", new_shouldfare=" + this.new_shouldfare + ", new_isout=" + this.new_isout + ", new_deliverystate="
				+ this.new_deliverystate + ", new_pos=" + this.new_pos + ", cwbordertypeid=" + this.cwbordertypeid + ", new_cwbordertypeid=" + this.new_cwbordertypeid + ", new_flowordertype="
				+ this.new_flowordertype + "]";
	}

}
