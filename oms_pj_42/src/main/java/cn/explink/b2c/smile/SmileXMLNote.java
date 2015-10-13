package cn.explink.b2c.smile;

import java.math.BigDecimal;

/**
 * 广州思迈签收，拒收，异常反馈的存储的对象，转为json——str
 * 
 * @author Administrator
 *
 */
public class SmileXMLNote {

	private String waybillNo; // 订单号
	private String state; // 思迈那边的反馈状态
	private BigDecimal replCost; // 代收款
	private String stateTime; // 状态产生时间
	private String operName; // 签收人
	private String SendClientLoge; // 发送客户标识（公司名称） 是否可配置？到时候确认
	private String request_code; // 请求指令
	private String AbnorInfo; // 发送异常的标示

	public String getAbnorInfo() {
		return AbnorInfo;
	}

	public void setAbnorInfo(String abnorInfo) {
		AbnorInfo = abnorInfo;
	}

	public String getRequest_code() {
		return request_code;
	}

	public void setRequest_code(String request_code) {
		this.request_code = request_code;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getReplCost() {
		return replCost;
	}

	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
	}

	public String getStateTime() {
		return stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getSendClientLoge() {
		return SendClientLoge;
	}

	public void setSendClientLoge(String sendClientLoge) {
		SendClientLoge = sendClientLoge;
	}

}
