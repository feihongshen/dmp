package cn.explink.domain;

public class SmtFareSettleDetailVO {

	private long stationId = 0;

	private String stationName = null;

	private long deliverId = 0;

	private String deliverName = null;

	private String cwb = null;

	private String shouldFee = null;

	private String receivedFee = null;

	private String feedbackResult = null;

	private String feedbackTime = null;

	private String systemAcceptTime = null;

	private String createTime = null;

	private String payType = null;

	public long getStationId() {
		return this.stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public long getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(long deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getShouldFee() {
		return this.shouldFee;
	}

	public void setShouldFee(String shouldFee) {
		this.shouldFee = shouldFee;
	}

	public String getReceivedFee() {
		return this.receivedFee;
	}

	public void setReceivedFee(String receivedFee) {
		this.receivedFee = receivedFee;
	}

	public String getFeedbackResult() {
		return this.feedbackResult;
	}

	public void setFeedbackResult(String feedbackResult) {
		this.feedbackResult = feedbackResult;
	}

	public String getFeedbackTime() {
		return this.feedbackTime;
	}

	public void setFeedbackTime(String feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getSystemAcceptTime() {
		return this.systemAcceptTime;
	}

	public void setSystemAcceptTime(String systemAcceptTime) {
		this.systemAcceptTime = systemAcceptTime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
