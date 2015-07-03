package cn.explink.domain;

import java.math.BigDecimal;
//用于支付申请-审核-确认环节的页面显示
public class ZhiFuApplyView {
	 
	private int applyid;
	private String cwb;
	private int customerid;
	private int cwbordertypeid;//订单类型
	private int applycwbordertypeid;//申请，审核时的订单类型
	private int flowordertype;
	private int branchid;
	private int paywayid;//支付方式
	private int applypaywayid;//申请，审核时的方式
	private BigDecimal receivablefee;//代收款
	private BigDecimal applyreceivablefee;//申请，审核时的代收款
	private int applyway;
	private int applystate;//审核所处的状态
	private int applyresult; //审核结果
	private int confirmstate;//确认状态
	private int confirmresult;//确认结果
	
	private int userid; //申请人
	private String applytime;//申请时间
	private String auditname;//审核人
	private String audittime;//审核时间
	private String feewaytyperemark;
	private String confirmname;
	private String confirmtime;
	
	public String getConfirmname() {
		return confirmname;
	}
	public void setConfirmname(String confirmname) {
		this.confirmname = confirmname;
	}
	public String getConfirmtime() {
		return confirmtime;
	}
	public void setConfirmtime(String confirmtime) {
		this.confirmtime = confirmtime;
	}
	//用于显示
	private String username;//申请人
	
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getConfirmstate() {
		return confirmstate;
	}
	public void setConfirmstate(int confirmstate) {
		this.confirmstate = confirmstate;
	}
	public int getConfirmresult() {
		return confirmresult;
	}
	public void setConfirmresult(int confirmresult) {
		this.confirmresult = confirmresult;
	}
	public int getApplyid() {
		return applyid;
	}
	public void setApplyid(int applyid) {
		this.applyid = applyid;
	}
	public int getApplyway() {
		return applyway;
	}
	public void setApplyway(int applyway) {
		this.applyway = applyway;
	}
	
	public String getFeewaytyperemark() {
		return feewaytyperemark;
	}
	public void setFeewaytyperemark(String feewaytyperemark) {
		this.feewaytyperemark = feewaytyperemark;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getApplyresult() {
		return applyresult;
	}
	public void setApplyresult(int applyresult) {
		this.applyresult = applyresult;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	public int getCwbordertypeid() {
		return cwbordertypeid;
	}
	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}
	public int getApplycwbordertypeid() {
		return applycwbordertypeid;
	}
	public void setApplycwbordertypeid(int applycwbordertypeid) {
		this.applycwbordertypeid = applycwbordertypeid;
	}
	public int getFlowordertype() {
		return flowordertype;
	}
	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}
	public int getBranchid() {
		return branchid;
	}
	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}
	public int getPaywayid() {
		return paywayid;
	}
	public void setPaywayid(int paywayid) {
		this.paywayid = paywayid;
	}
	public int getApplypaywayid() {
		return applypaywayid;
	}
	public void setApplypaywayid(int applypaywayid) {
		this.applypaywayid = applypaywayid;
	}
	public BigDecimal getReceivablefee() {
		return receivablefee;
	}
	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}
	public BigDecimal getApplyreceivablefee() {
		return applyreceivablefee;
	}
	public void setApplyreceivablefee(BigDecimal applyreceivablefee) {
		this.applyreceivablefee = applyreceivablefee;
	}
	public int getApplystate() {
		return applystate;
	}
	public void setApplystate(int applystate) {
		this.applystate = applystate;
	}
	
	
	
}
