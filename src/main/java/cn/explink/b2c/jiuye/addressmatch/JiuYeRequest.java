package cn.explink.b2c.jiuye.addressmatch;

import org.codehaus.jackson.annotate.JsonProperty;

import cn.explink.b2c.jiuye.addressmatch.Content;

public class JiuYeRequest {
	@JsonProperty(value = "RequestName")
	private String requestName;
	@JsonProperty(value = "DelveryCode")
	private String delveryCode;
	@JsonProperty(value = "Sign")
	private String sign;
	@JsonProperty(value = "TimeStamp")
	private String timeStamp;
	@JsonProperty(value = "Content")
	private Content content;
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getDelveryCode() {
		return delveryCode;
	}
	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
}
