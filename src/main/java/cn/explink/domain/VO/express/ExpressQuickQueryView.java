package cn.explink.domain.VO.express;


/**
 * 预订单的快速查询
 * @author jiangyu 2015年8月8日
 *
 */
public class ExpressQuickQueryView {
	/*
	 * 预订单编号(not null)
	 */
	private String preOrderNo;
	/*
	 * 执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)
	 */
	private String excuteState="";
	/*
	 *
	 */
	private String sendPerson;
	/*
	 * 手机号码
	 */
	private String cellphone;
	/*
	 * 固定电话
	 */
	private String telephone;
	/*
	 * 取件地址
	 */
	private String collectAddress;
	/*
	 * 原因
	 */
	private String reason;
	/*
	 * 分配站点名称
	 */
	private String branchName;
	/*
	 * 省公司处理预订单时间
	 */
	private String handleTime;
	/*
	 * 处理人姓名
	 */
	private String handleUserName;
	/*
	 * 分配小件员的时间
	 */
	private String distributeDelivermanTime;
	/*
	 * 生成时间
	 */
	private String createTime;
	/*
	 * 预约时间
	 */
	private String arrangeTime;
	/*
	 * 小件员姓名
	 */
	private String delivermanName;
	/*
	 * 分配小件员的操作人姓名
	 */
	private String distributeUserName;
	/*
	 * 快递单号（也是订单表中的订单号）
	 */
	private String orderNo;
	/*
	 * 一级原因
	 */
	private String feedbackFirstReason;
	/*
	 * 二级原因
	 */
	private String feedbackSecondReason;
	/*
	 * 反馈备注
	 */
	private String feedbackRemark;
	/*
	 * 反馈人姓名
	 */
	private String feedbackUserName;
	/*
	 * 反馈时间
	 */
	private String feedbackTime;

	/*
	 * 预计下次揽件时间
	 */
	private String nextPickTime;

	/*
	 * 托物资料-其他
	 */
	private String other;
	
	public ExpressQuickQueryView() {
		// TODO Auto-generated constructor stub
	}

	public String getPreOrderNo() {
		return preOrderNo;
	}

	public void setPreOrderNo(String preOrderNo) {
		this.preOrderNo = preOrderNo;
	}

	public String getExcuteState() {
		return excuteState;
	}

	public void setExcuteState(String excuteState) {
		this.excuteState = excuteState;
	}

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCollectAddress() {
		return collectAddress;
	}

	public void setCollectAddress(String collectAddress) {
		this.collectAddress = collectAddress;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public String getDistributeDelivermanTime() {
		return distributeDelivermanTime;
	}

	public void setDistributeDelivermanTime(String distributeDelivermanTime) {
		this.distributeDelivermanTime = distributeDelivermanTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getArrangeTime() {
		return arrangeTime;
	}

	public void setArrangeTime(String arrangeTime) {
		this.arrangeTime = arrangeTime;
	}

	public String getDelivermanName() {
		return delivermanName;
	}

	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}

	public String getDistributeUserName() {
		return distributeUserName;
	}

	public void setDistributeUserName(String distributeUserName) {
		this.distributeUserName = distributeUserName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFeedbackFirstReason() {
		return feedbackFirstReason;
	}

	public void setFeedbackFirstReason(String feedbackFirstReason) {
		this.feedbackFirstReason = feedbackFirstReason;
	}

	public String getFeedbackSecondReason() {
		return feedbackSecondReason;
	}

	public void setFeedbackSecondReason(String feedbackSecondReason) {
		this.feedbackSecondReason = feedbackSecondReason;
	}

	public String getFeedbackRemark() {
		return feedbackRemark;
	}

	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}

	public String getFeedbackUserName() {
		return feedbackUserName;
	}

	public void setFeedbackUserName(String feedbackUserName) {
		this.feedbackUserName = feedbackUserName;
	}

	public String getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(String feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getNextPickTime() {
		return nextPickTime;
	}

	public void setNextPickTime(String nextPickTime) {
		this.nextPickTime = nextPickTime;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}
