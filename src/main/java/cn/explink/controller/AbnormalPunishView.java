package cn.explink.controller;

public class AbnormalPunishView {
	private String questNo;//问题件单号
	private String cwb;//主表订单号
	private String cwbprice;//订单价格
	private String customername;//供货商名字
	private String  cwbtype;//订单类型
	private String creuser;//创建人
	private String cretime;//创建时间
	private long abnormaltypeid;//问题件的类型
	private String abnormaltype;//问题件类型
	private long dealstateid;//问题件处理状态id
	private String dealstate;//问题件处理状态
	private String isfind;//是否已经找到
	private String dealresult;//处理结果
	
	
	public String getDealresult() {
		return dealresult;
	}
	public void setDealresult(String dealresult) {
		this.dealresult = dealresult;
	}
	public String getIsfind() {
		return isfind;
	}
	public void setIsfind(String isfind) {
		this.isfind = isfind;
	}
	public String getQuestNo() {
		return questNo;
	}
	public void setQuestNo(String questNo) {
		this.questNo = questNo;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getCwbprice() {
		return cwbprice;
	}
	public void setCwbprice(String cwbprice) {
		this.cwbprice = cwbprice;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getCwbtype() {
		return cwbtype;
	}
	public void setCwbtype(String cwbtype) {
		this.cwbtype = cwbtype;
	}
	public String getCreuser() {
		return creuser;
	}
	public void setCreuser(String creuser) {
		this.creuser = creuser;
	}
	public String getCretime() {
		return cretime;
	}
	public void setCretime(String cretime) {
		this.cretime = cretime;
	}
	public long getAbnormaltypeid() {
		return abnormaltypeid;
	}
	public void setAbnormaltypeid(long abnormaltypeid) {
		this.abnormaltypeid = abnormaltypeid;
	}
	public String getAbnormaltype() {
		return abnormaltype;
	}
	public void setAbnormaltype(String abnormaltype) {
		this.abnormaltype = abnormaltype;
	}
	public long getDealstateid() {
		return dealstateid;
	}
	public void setDealstateid(long dealstateid) {
		this.dealstateid = dealstateid;
	}
	public String getDealstate() {
		return dealstate;
	}
	public void setDealstate(String dealstate) {
		this.dealstate = dealstate;
	}
	

}
