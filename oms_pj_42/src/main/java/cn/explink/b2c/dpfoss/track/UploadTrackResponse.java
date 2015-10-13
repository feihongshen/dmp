package cn.explink.b2c.dpfoss.track;

import java.io.Serializable;

public class UploadTrackResponse implements Serializable {
	private String traceId;
	private String success;
	private String detailsInfo;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getDetailsInfo() {
		return detailsInfo;
	}

	public void setDetailsInfo(String detailsInfo) {
		this.detailsInfo = detailsInfo;
	}
}
