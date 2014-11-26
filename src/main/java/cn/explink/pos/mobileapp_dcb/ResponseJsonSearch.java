package cn.explink.pos.mobileapp_dcb;

public class ResponseJsonSearch {
	private String cwb;
	private String errcode;
	private String errmsg;
	private String cwbstatus; // 订单状态 派送中、已签收、拒收、滞留
	private String cwbremark;

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getCwbstatus() {
		return cwbstatus;
	}

	public void setCwbstatus(String cwbstatus) {
		this.cwbstatus = cwbstatus;
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

}
