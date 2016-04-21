package cn.explink.domain;

public class TpsCwbFlowVo {
	private String cwb;
	private String scancwb;
	private long flowordertype;
	private String errinfo;
	private int state;
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getScancwb() {
		return scancwb;
	}
	public void setScancwb(String scancwb) {
		this.scancwb = scancwb;
	}
	public long getFlowordertype() {
		return flowordertype;
	}
	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}
	public String getErrinfo() {
		return errinfo;
	}
	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
