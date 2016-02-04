package cn.explink.b2c.suning.responsedto;

public class ResponseData {
	private ResponseBody body;
	private String spCode;
	private String contentMesDigest;
	private String time;
	private String success;
	private String failedReason;
	public ResponseBody getBody() {
		return body;
	}
	public void setBody(ResponseBody body) {
		this.body = body;
	}
	public String getSpCode() {
		return spCode;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public String getContentMesDigest() {
		return contentMesDigest;
	}
	public void setContentMesDigest(String contentMesDigest) {
		this.contentMesDigest = contentMesDigest;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getFailedReason() {
		return failedReason;
	}
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	
}
