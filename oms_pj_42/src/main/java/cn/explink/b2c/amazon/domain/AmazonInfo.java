package cn.explink.b2c.amazon.domain;

/**
 * 订单
 * 
 * @author Administrator
 *
 */
public class AmazonInfo {

	private String messageReferenceNum;// 加密发货单号 Dh45pSlJR
	private String carrierTrackingNum;// 订单号 190007245516
	private String sendPartyName;// 发货人 JoyoAmazon
	private String sendLine1;// 发货地址 B1-B2, No.1 Pu Bei Rd, North Kai C
	private String sendCity;// 发货市 Huangpu
	private String sendStateProvinceCode;// 发货省 Guangzhou
	private String sendPostalCode;// 发货邮编 510530
	private String sendCountryCode;// 发货国籍 CN

	private String signPartyName;// 收件人 张立业
	private String signLine1;// 收件人地址 广顺北大街33号 望京华联4层阿迪达斯专
	private String signCity;// 收件人市 北京市
	private String signStateProvinceCode;// 收件人省 北京
	private String signPostalCode;// 收件人邮编 100020
	private String signCountryCode;// 收件人国籍 CN

	private String status;// 订单状态，转换后 SD
	private String statusReason;// 原因码 BG
	private String dateTimePeriodValue;// 订单状态时间 201210190451
	private String dateTime5PeriodValue;// 订单状态时间 201210190451 + 5分钟

	private String pmcr_CODE;// 未刷卡原因

	private String deliveryName;// 小件员姓名
	private String deliveryPhone;// 小件员电话
	private String branchName;// 站点名称
	private String branchPhone;// 站点电话
	private String branchAddress;// 站点电话

	private int isCach;// 是否使用现金

	private int isTuotou;// 是否妥投，且金额大于0

	private String payType;// 支付方式

	public int getIsTuotou() {
		return isTuotou;
	}

	public void setIsTuotou(int isTuotou) {
		this.isTuotou = isTuotou;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMessageReferenceNum() {
		return messageReferenceNum;
	}

	public void setMessageReferenceNum(String messageReferenceNum) {
		this.messageReferenceNum = messageReferenceNum;
	}

	public String getCarrierTrackingNum() {
		return carrierTrackingNum;
	}

	public void setCarrierTrackingNum(String carrierTrackingNum) {
		this.carrierTrackingNum = carrierTrackingNum;
	}

	public String getSendPartyName() {
		return sendPartyName;
	}

	public void setSendPartyName(String sendPartyName) {
		this.sendPartyName = sendPartyName;
	}

	public String getSendLine1() {
		return sendLine1;
	}

	public void setSendLine1(String sendLine1) {
		this.sendLine1 = sendLine1;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getSendStateProvinceCode() {
		return sendStateProvinceCode;
	}

	public void setSendStateProvinceCode(String sendStateProvinceCode) {
		this.sendStateProvinceCode = sendStateProvinceCode;
	}

	public String getSendPostalCode() {
		return sendPostalCode;
	}

	public void setSendPostalCode(String sendPostalCode) {
		this.sendPostalCode = sendPostalCode;
	}

	public String getSendCountryCode() {
		return sendCountryCode;
	}

	public void setSendCountryCode(String sendCountryCode) {
		this.sendCountryCode = sendCountryCode;
	}

	public String getSignPartyName() {
		return signPartyName;
	}

	public void setSignPartyName(String signPartyName) {
		this.signPartyName = signPartyName;
	}

	public String getSignLine1() {
		return signLine1;
	}

	public void setSignLine1(String signLine1) {
		this.signLine1 = signLine1;
	}

	public String getSignCity() {
		return signCity;
	}

	public void setSignCity(String signCity) {
		this.signCity = signCity;
	}

	public String getSignStateProvinceCode() {
		return signStateProvinceCode;
	}

	public void setSignStateProvinceCode(String signStateProvinceCode) {
		this.signStateProvinceCode = signStateProvinceCode;
	}

	public String getSignPostalCode() {
		return signPostalCode;
	}

	public void setSignPostalCode(String signPostalCode) {
		this.signPostalCode = signPostalCode;
	}

	public String getSignCountryCode() {
		return signCountryCode;
	}

	public void setSignCountryCode(String signCountryCode) {
		this.signCountryCode = signCountryCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public String getDateTimePeriodValue() {
		return dateTimePeriodValue;
	}

	public void setDateTimePeriodValue(String dateTimePeriodValue) {
		this.dateTimePeriodValue = dateTimePeriodValue;
	}

	public String getPmcr_CODE() {
		return pmcr_CODE;
	}

	public void setPmcr_CODE(String pmcr_CODE) {
		this.pmcr_CODE = pmcr_CODE;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryPhone() {
		return deliveryPhone;
	}

	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchPhone() {
		return branchPhone;
	}

	public void setBranchPhone(String branchPhone) {
		this.branchPhone = branchPhone;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public int getIsCach() {
		return isCach;
	}

	public void setIsCach(int isCach) {
		this.isCach = isCach;
	}

	public String getDateTime5PeriodValue() {
		return dateTime5PeriodValue;
	}

	public void setDateTime5PeriodValue(String dateTime5PeriodValue) {
		this.dateTime5PeriodValue = dateTime5PeriodValue;
	}

}
