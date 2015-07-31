package cn.explink.domain;


public class CwbOXODetailBean {
	private String cwb; //订单号
	private String customerName; //供应商
	private String creDate; //生产时间
	private String cwbState; //执行状态
	private double receivablefee; //应收金额
	private String branchName; //揽件/配送站点
	private String payWay; //支付方式
	private String oxoType;//执行类型 1揽收 2配送
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCreDate() {
		return creDate;
	}
	public void setCreDate(String creDate) {
		this.creDate = creDate;
	}
	public String getCwbState() {
		return cwbState;
	}
	public void setCwbState(String cwbState) {
		this.cwbState = cwbState;
	}
	public double getReceivablefee() {
		return receivablefee;
	}
	public void setReceivablefee(double receivablefee) {
		this.receivablefee = receivablefee;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getOxoType() {
		return oxoType;
	}
	public void setOxoType(String oxoType) {
		this.oxoType = oxoType;
	}
}
