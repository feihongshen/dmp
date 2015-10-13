package cn.explink.b2c.dpfoss.sign;

import java.util.Date;

/**
 * 德邦 XML的节点
 * 
 * @author Administrator
 *
 */
public class UploadSignRequest {

	private String signUpId;
	private String waybillNo;
	private String agentCompanyName;
	private String agentCompanyCode;
	private String agentOrgName;
	private String agentOrgCode;
	private String receiveTime;

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(String signUpTime) {
		this.signUpTime = signUpTime;
	}

	private String deliveryTime;
	private String deliveryUserName;
	private String signUpTime;

	public String getSignUpId() {
		return signUpId;
	}

	public void setSignUpId(String signUpId) {
		this.signUpId = signUpId;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getAgentCompanyName() {
		return agentCompanyName;
	}

	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
	}

	public String getAgentCompanyCode() {
		return agentCompanyCode;
	}

	public void setAgentCompanyCode(String agentCompanyCode) {
		this.agentCompanyCode = agentCompanyCode;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public String getAgentOrgCode() {
		return agentOrgCode;
	}

	public void setAgentOrgCode(String agentOrgCode) {
		this.agentOrgCode = agentOrgCode;
	}

	public String getDeliveryUserName() {
		return deliveryUserName;
	}

	public void setDeliveryUserName(String deliveryUserName) {
		this.deliveryUserName = deliveryUserName;
	}

	public String getSignUpUserName() {
		return signUpUserName;
	}

	public void setSignUpUserName(String signUpUserName) {
		this.signUpUserName = signUpUserName;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getSignUpType() {
		return signUpType;
	}

	public void setSignUpType(String signUpType) {
		this.signUpType = signUpType;
	}

	public String getExceptionDescribe() {
		return exceptionDescribe;
	}

	public void setExceptionDescribe(String exceptionDescribe) {
		this.exceptionDescribe = exceptionDescribe;
	}

	private String signUpUserName;
	private int goodsNum;
	private String signUpType;
	private String exceptionDescribe;

}
