package cn.explink.domain;

public class SmtOrder {

	private long cwbId = -1;

	private String cwb = null;

	private String sysRecvTime = " ";

	private String curBranRecvTime = " ";

	private String dispatchTime = null;

	private String customerName = null;

	private String phone = null;

	private String address = null;

	private double receivedFee = 0;

	private String matchBranch = "";

	private long deliver = -1;

	private String strDeliver = null;

	public long getCwbId() {
		return this.cwbId;
	}

	public void setCwbId(long cwbId) {
		this.cwbId = cwbId;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getSysRecvTime() {
		return this.sysRecvTime;
	}

	public void setSysRecvTime(String sysRecvTime) {
		this.sysRecvTime = sysRecvTime;
	}

	public String getCurBranRecvTime() {
		return this.curBranRecvTime;
	}

	public void setCurBranRecvTime(String curBranRecvTime) {
		this.curBranRecvTime = curBranRecvTime;
	}

	public String getDispatchTime() {
		return this.dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getReceivedFee() {
		return this.receivedFee;
	}

	public void setReceivedFee(double receivedFee) {
		this.receivedFee = receivedFee;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMatchBranch() {
		return this.matchBranch;
	}

	public void setMatchBranch(String matchBranch) {
		this.matchBranch = matchBranch;
	}

	public long getDeliver() {
		return this.deliver;
	}

	public void setDeliver(long deliver) {
		this.deliver = deliver;
	}

	public String getStrDeliver() {
		return this.strDeliver;
	}

	public void setStrDeliver(String strDeliver) {
		this.strDeliver = strDeliver;
	}

}
