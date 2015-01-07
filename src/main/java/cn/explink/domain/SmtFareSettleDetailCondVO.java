package cn.explink.domain;

public class SmtFareSettleDetailCondVO {

	private int optTimeType = 0;

	private String startTime = null;

	private String endTime = null;

	private long venderId = 0;

	private long stationId = 0;

	private long deliverId = 0;

	public int getOptTimeType() {
		return this.optTimeType;
	}

	public void setOptTimeType(int optTimeType) {
		this.optTimeType = optTimeType;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(long venderId) {
		this.venderId = venderId;
	}

	public long getStationId() {
		return this.stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(long deliverId) {
		this.deliverId = deliverId;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
