package cn.explink.b2c.jiuye.jsondto;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiuYe_request {
	@JsonProperty(value = "RequestName") 
	private String requestName;
	@JsonProperty(value = "DelveryCode") 
	private String delveryCode;
	@JsonProperty(value = "TimeStamp") 
	private String timeStamp;
	@JsonProperty(value = "Sign")
	private String sign;
	@JsonProperty(value = "Content")
	private JiuYeContent content;
	
	public String getDelveryCode() {
		return delveryCode;
	}
	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public JiuYeContent getContent() {
		return content;
	}
	public void setContent(JiuYeContent content) {
		this.content = content;
	}
}