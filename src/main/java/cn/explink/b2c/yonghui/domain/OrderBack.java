package cn.explink.b2c.yonghui.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderBack {
	private String errCode;
	private String errMsg;
	private List<String> errlist = new ArrayList<String>();

	public String getErrCode() {
		return this.errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public List<String> getErrlist() {
		return this.errlist;
	}

	public void setErrlist(List<String> errlist) {
		this.errlist = errlist;
	}

}
