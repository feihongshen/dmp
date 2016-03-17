package cn.explink.b2c.auto.order.service;

public class AutoOrderStatusTmpVo {
	private String cwb;
	private String transportno;//箱号
	private String operatetype;
	private String msg;
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getOperatetype() {
		return operatetype;
	}
	public void setOperatetype(String operatetype) {
		this.operatetype = operatetype;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTransportno() {
		return transportno;
	}
	public void setTransportno(String transportno) {
		this.transportno = transportno;
	}
	
	
}
