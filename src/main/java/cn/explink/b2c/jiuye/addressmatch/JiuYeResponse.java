package cn.explink.b2c.jiuye.addressmatch;

import org.codehaus.jackson.annotate.JsonProperty;

public class JiuYeResponse {
	@JsonProperty(value = "RequestName")
	private String requestName;
	@JsonProperty(value = "Partner")
	private String partner;
	@JsonProperty(value = "Success")
	private boolean success;
	@JsonProperty(value = "IsArrive")
	private boolean isArrive;
	@JsonProperty(value = "SiteNum")
	private String siteNum; 
	@JsonProperty(value = "SiteName")
	private String siteName;
	@JsonProperty(value = "Msg")
	private String msg;
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
	public boolean isIsArrive(){
		return isArrive;
	}
	public void setIsArrive(boolean isArrive) {
		this.isArrive = isArrive;
	}
	public String getSiteNum() {
		return siteNum;
	}
	public void setSiteNum(String siteNum) {
		this.siteNum = siteNum;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
