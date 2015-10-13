package cn.explink.b2c.explink.code_down;

/**
 * 下游存储易派信息发送上游的json格式
 * 
 * @author Administrator
 *
 */
public class EPaiXMLNote {

	private String userCode;
	private String requestTime;
	private String sign;

	private String cwb;
	private String operatortime; // 操作时间
	private String flowordertype; // 操作状态
	private String deliverystate; // 配送结果
	private int paytype; // 支付方式
	private String payremark;
	private String floworderdetail;// 操作详情描述
	private String exptcode; // 异常码

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getOperatortime() {
		return operatortime;
	}

	public void setOperatortime(String operatortime) {
		this.operatortime = operatortime;
	}

	public String getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(String flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(String deliverystate) {
		this.deliverystate = deliverystate;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public String getPayremark() {
		return payremark;
	}

	public void setPayremark(String payremark) {
		this.payremark = payremark;
	}

	public String getFloworderdetail() {
		return floworderdetail;
	}

	public void setFloworderdetail(String floworderdetail) {
		this.floworderdetail = floworderdetail;
	}

	public String getExptcode() {
		return exptcode;
	}

	public void setExptcode(String exptcode) {
		this.exptcode = exptcode;
	}

	public String getExptmsg() {
		return exptmsg;
	}

	public void setExptmsg(String exptmsg) {
		this.exptmsg = exptmsg;
	}

	private String exptmsg; // 异常状态描述

}
