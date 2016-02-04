package cn.explink.b2c.suning.requestdto;

public class RequestData {
	private RequestBody body;
	private String spCode;
	private String contentMesDigest;
	private String time;
	public RequestBody getBody() {
		return body;
	}
	public void setBody(RequestBody body) {
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
	
}
