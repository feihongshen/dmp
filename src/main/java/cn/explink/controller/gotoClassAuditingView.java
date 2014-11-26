package cn.explink.controller;

import java.math.BigDecimal;

public class gotoClassAuditingView {
	private long id;
	private String auditingtime;
	private BigDecimal payupamount;
	private BigDecimal payupamount_pos;
	private long receivedfeeuser;
	private long branchid;
	private long payupid;
	private long deliverealuser;
	private int state;
	private String deliverealuser_name;
	private String receivedfeeuser_name;

	private int deliverpayuptype; // 小件员交款类型 0非小件员交款 1网银（网银需要小票号） 2POS 3现金
	private BigDecimal deliverpayupamount; // 小件员交款金额 小数点两位
	private String deliverpayupbanknum; // 小件员交款网银的小票号
	private int deliverpayupapproved; // 小件员交款审核状态 0 未审核 1 已审核

	private BigDecimal deliverpayuparrearage; // 小件员欠款 小数点两位
	private int deliverpayuparrearagetype; // 小件员交欠款类型 0非小件员交款 1网银（网银需要小票号） 2POS
											// 3现金
	private BigDecimal deliverpayuparrearageamount; // 小件员上交欠款 小数点两位
	private String deliverpayuparrearagebanknum; // 小件员交欠款网银的小票号
	private String deliverpayuparrearagetime; // 小件员交欠款时间
	private int deliverpayuparrearageapproved; // 小件员交罚款审核状态 0 未审核 1 已审核

	private String updatetime; // 修改订单的时间，代表本次归班中的订单最后一次修改时间，任何本次归班中的订单更新都将更新这个时间

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public BigDecimal getPayupamount() {
		return payupamount;
	}

	public void setPayupamount(BigDecimal payupamount) {
		this.payupamount = payupamount;
	}

	public BigDecimal getPayupamount_pos() {
		return payupamount_pos;
	}

	public void setPayupamount_pos(BigDecimal payupamount_pos) {
		this.payupamount_pos = payupamount_pos;
	}

	public long getReceivedfeeuser() {
		return receivedfeeuser;
	}

	public void setReceivedfeeuser(long receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getPayupid() {
		return payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public long getDeliverealuser() {
		return deliverealuser;
	}

	public void setDeliverealuser(long deliverealuser) {
		this.deliverealuser = deliverealuser;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDeliverealuser_name() {
		return deliverealuser_name;
	}

	public void setDeliverealuser_name(String deliverealuser_name) {
		this.deliverealuser_name = deliverealuser_name;
	}

	public String getReceivedfeeuser_name() {
		return receivedfeeuser_name;
	}

	public void setReceivedfeeuser_name(String receivedfeeuser_name) {
		this.receivedfeeuser_name = receivedfeeuser_name;
	}

	public int getDeliverpayuptype() {
		return deliverpayuptype;
	}

	public void setDeliverpayuptype(int deliverpayuptype) {
		this.deliverpayuptype = deliverpayuptype;
	}

	public BigDecimal getDeliverpayupamount() {
		return deliverpayupamount;
	}

	public void setDeliverpayupamount(BigDecimal deliverpayupamount) {
		this.deliverpayupamount = deliverpayupamount;
	}

	public String getDeliverpayupbanknum() {
		return deliverpayupbanknum;
	}

	public void setDeliverpayupbanknum(String deliverpayupbanknum) {
		this.deliverpayupbanknum = deliverpayupbanknum;
	}

	public int getDeliverpayupapproved() {
		return deliverpayupapproved;
	}

	public void setDeliverpayupapproved(int deliverpayupapproved) {
		this.deliverpayupapproved = deliverpayupapproved;
	}

	public BigDecimal getDeliverpayuparrearage() {
		return deliverpayuparrearage;
	}

	public void setDeliverpayuparrearage(BigDecimal deliverpayuparrearage) {
		this.deliverpayuparrearage = deliverpayuparrearage;
	}

	public int getDeliverpayuparrearagetype() {
		return deliverpayuparrearagetype;
	}

	public void setDeliverpayuparrearagetype(int deliverpayuparrearagetype) {
		this.deliverpayuparrearagetype = deliverpayuparrearagetype;
	}

	public BigDecimal getDeliverpayuparrearageamount() {
		return deliverpayuparrearageamount;
	}

	public void setDeliverpayuparrearageamount(BigDecimal deliverpayuparrearageamount) {
		this.deliverpayuparrearageamount = deliverpayuparrearageamount;
	}

	public String getDeliverpayuparrearagebanknum() {
		return deliverpayuparrearagebanknum;
	}

	public void setDeliverpayuparrearagebanknum(String deliverpayuparrearagebanknum) {
		this.deliverpayuparrearagebanknum = deliverpayuparrearagebanknum;
	}

	public String getDeliverpayuparrearagetime() {
		return deliverpayuparrearagetime;
	}

	public void setDeliverpayuparrearagetime(String deliverpayuparrearagetime) {
		this.deliverpayuparrearagetime = deliverpayuparrearagetime;
	}

	public int getDeliverpayuparrearageapproved() {
		return deliverpayuparrearageapproved;
	}

	public void setDeliverpayuparrearageapproved(int deliverpayuparrearageapproved) {
		this.deliverpayuparrearageapproved = deliverpayuparrearageapproved;
	}

}
