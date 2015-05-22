package cn.explink.vo;

public class Information {
	private String error;//匹配错误信息描述
	private String cwb;//错误的订单
	private String branch;//错误的站点
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	
}
