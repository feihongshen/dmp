package cn.explink.domain;

public class OverdueExMoDetailCondVO {

	private long orgId = 0;

	private long venderId = 0;

	private int optTimeType = 0;

	private String startTime = null;

	private String endTime = null;

	private int showColIndex = 0;

	private boolean enableTEQuery = false;

	public long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

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

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getShowColIndex() {
		return this.showColIndex;
	}

	public void setShowColIndex(int showColIndex) {
		this.showColIndex = showColIndex;
	}

	public boolean isEnableTEQuery() {
		return this.enableTEQuery;
	}

	public void setEnableTEQuery(boolean enableTEQuery) {
		this.enableTEQuery = enableTEQuery;
	}

	public long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(long venderId) {
		this.venderId = venderId;
	}

}
