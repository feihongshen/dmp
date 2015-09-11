package cn.explink.b2c.jiuye.jsondto;

import org.codehaus.jackson.annotate.JsonProperty;

public class JiuYe_response {
	@JsonProperty(value = "RequestName") 
	private String requestName;
	@JsonProperty(value = "Partner") 
	private String partner;
	@JsonProperty(value = "Success") 
	private boolean success;
	@JsonProperty(value = "Msg") 
	private String msg;
	@JsonProperty(value = "Count") 
	private int count;
	@JsonProperty(value = "Code") 
	private String code;
	@JsonProperty(value = "Content") 
	private String content;
	
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}