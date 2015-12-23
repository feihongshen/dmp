package cn.explink.domain.VO.express;

import cn.explink.enumutil.express.ExcuteStateEnum;

public class PreOrderQueryDateVO {
	/*
	 * 预订单id
	 */
	private int id;
	/*
	 * 预订单编号
	 */
	private String preOrderNo;
	/*
	 * 执行状态代码
	 */
	private int excuteState;
	/*
	 * 执行状态名称
	 */
	private String excuteStateName;

	/*
	 * 寄件人
	 */
	private String sendPerson;
	/*
	 * 手机号码
	 */
	private String cellphone;
	/*
	 * 固话
	 */
	private String telephone;
	/*
	 * 取件地址
	 */
	private String collectAddress;
	/*
	 * 站点id
	 */
	private int branchId;
	/*
	 * 站点名字
	 */
	private String branchName;
	/*
	 * 反馈人id
	 */
	private int feedbackUserId;
	/*
	 * 反馈站点id
	 */
	private int feedbackBranchId;
	/*
	 * 反馈站点
	 */
	private String feedbackBranchName;
	/*
	 * 揽件人id
	 */
	private int delivermanId;
	/*
	 * 揽件人名字
	 */
	private String delivermanName;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPreOrderNo() {
		return this.preOrderNo;
	}

	public void setPreOrderNo(String preOrderNo) {
		this.preOrderNo = preOrderNo;
	}

	public int getExcuteState() {
		return this.excuteState;
	}

	public void setExcuteState(int excuteState) {
		this.excuteState = excuteState;
		this.excuteStateName = ExcuteStateEnum.getTextByValue(excuteState);
	}

	public String getExcuteStateName() {
		return this.excuteStateName;
	}

	public String getSendPerson() {
		return this.sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCollectAddress() {
		return this.collectAddress;
	}

	public void setCollectAddress(String collectAddress) {
		this.collectAddress = collectAddress;
	}

	public int getBranchId() {
		return this.branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getFeedbackBranchId() {
		return this.feedbackBranchId;
	}

	public void setFeedbackBranchId(int feedbackBranchId) {
		this.feedbackBranchId = feedbackBranchId;
	}

	public String getFeedbackBranchName() {
		return this.feedbackBranchName;
	}

	public void setFeedbackBranchName(String feedbackBranchName) {
		this.feedbackBranchName = feedbackBranchName;
	}

	public void setExcuteStateName(String excuteStateName) {
		this.excuteStateName = excuteStateName;
	}

	public int getFeedbackUserId() {
		return this.feedbackUserId;
	}

	public void setFeedbackUserId(int feedbackUserId) {
		this.feedbackUserId = feedbackUserId;
	}

	public int getDelivermanId() {
		return this.delivermanId;
	}

	public void setDelivermanId(int delivermanId) {
		this.delivermanId = delivermanId;
	}

	public String getDelivermanName() {
		return this.delivermanName;
	}

	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}

}
