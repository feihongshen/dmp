package cn.explink.controller;

public class CwbOrderImportDTO {
	String cwb; // 订单号
	String signinman; // 签收人
	String signintime; // 签收时间
	String cwbremark; // 备注

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getSigninman() {
		return signinman;
	}

	public void setSigninman(String signinman) {
		this.signinman = signinman;
	}

	public String getSignintime() {
		return signintime;
	}

	public void setSignintime(String signintime) {
		this.signintime = signintime;
	}

}
