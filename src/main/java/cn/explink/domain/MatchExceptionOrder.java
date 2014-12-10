package cn.explink.domain;

public class MatchExceptionOrder {

	private String cwb = null;

	private String reportOutAreaTime = null;

	private long reportOutAreaBranchId = 0;

	private String reportOutAreaBranchName = null;

	private long reportOutAreaUserId = 0;

	private String reportOutAreaUserName = null;

	private long matchBranchId = 0;

	private String matchBranchName = "";

	private String customerName = null;

	private String customerPhone = null;

	private String customerAddress = null;

	private double receivedFee = 0;

	private int outareaFlag = 0;

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getReportOutAreaTime() {
		return this.reportOutAreaTime;
	}

	public void setReportOutAreaTime(String reportOutAreaTime) {
		this.reportOutAreaTime = reportOutAreaTime;
	}

	public long getReportOutAreaBranchId() {
		return this.reportOutAreaBranchId;
	}

	public void setReportOutAreaBranchId(long reportOutAreaBranchId) {
		this.reportOutAreaBranchId = reportOutAreaBranchId;
	}

	public String getReportOutAreaBranchName() {
		return this.reportOutAreaBranchName;
	}

	public void setReportOutAreaBranchName(String reportOutAreaBranchName) {
		this.reportOutAreaBranchName = reportOutAreaBranchName;
	}

	public long getMatchBranchId() {
		return this.matchBranchId;
	}

	public void setMatchBranchId(long matchBranchId) {
		this.matchBranchId = matchBranchId;
	}

	public String getMatchBranchName() {
		return this.matchBranchName;
	}

	public void setMatchBranchName(String matchBranchName) {
		this.matchBranchName = matchBranchName;
	}

	public long getReportOutAreaUserId() {
		return this.reportOutAreaUserId;
	}

	public void setReportOutAreaUserId(long reportOutAreaUserId) {
		this.reportOutAreaUserId = reportOutAreaUserId;
	}

	public String getReportOutAreaUserName() {
		return this.reportOutAreaUserName;
	}

	public void setReportOutAreaUserName(String reportOutAreaUserName) {
		this.reportOutAreaUserName = reportOutAreaUserName;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return this.customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public double getReceivedFee() {
		return this.receivedFee;
	}

	public void setReceivedFee(double receivedFee) {
		this.receivedFee = receivedFee;
	}

	public int getOutareaFlag() {
		return this.outareaFlag;
	}

	public void setOutareaFlag(int outareaFlag) {
		this.outareaFlag = outareaFlag;
	}

}
