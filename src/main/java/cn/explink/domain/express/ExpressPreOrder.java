package cn.explink.domain.express;

import java.util.Date;

import cn.explink.util.poi.excel.annotation.Excel;

public class ExpressPreOrder {
	/*
	 * 主键 (not null)
	 */
	private int id;
	/*
	 * 预订单编号(not null)
	 */
	@Excel(exportName = "预订单号", exportFieldWidth = 20)
	private String preOrderNo;
	/*
	 * 预订单状态（0：正常，1：关闭，2：退回）
	 */
	private int status;
	/*
	 * 执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)
	 */
	private int excuteState;
	/*
	 * add by wangzhiyu
	 * 等考俊回来了需要将实体改成vo进行传值
	 * 执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)
	 */
	private String excuteStateStr;

	/*
	 *
	 */
	@Excel(exportName = "寄件人", exportFieldWidth = 20)
	private String sendPerson;
	/*
	 * 手机号码
	 */
	@Excel(exportName = "手机号", exportFieldWidth = 20)
	private String cellphone;
	/*
	 * 固定电话
	 */
	@Excel(exportName = "固话", exportFieldWidth = 20)
	private String telephone;
	/*
	 * 取件地址
	 */
	@Excel(exportName = "取件地址", exportFieldWidth = 20)
	private String collectAddress;
	/*
	 * 原因
	 */
	private String reason;
	/*
	 * 分配站点id
	 */
	private int branchId;
	/*
	 * 分配站点名称
	 */
	private String branchName;
	/*
	 * 省公司处理预订单时间
	 */
	private Date handleTime;
	/*
	 * 处理人id
	 */
	private int handleUserId;
	/*
	 * 处理人姓名
	 */
	private String handleUserName;
	/*
	 * 分配小件员的时间
	 */
	private Date distributeDelivermanTime;
	/*
	 * 生成时间
	 */
	private Date createTime;
	/*
	 * 预约时间
	 */
	@Excel(exportName = "预约时间", exportFieldWidth = 20)
	private Date arrangeTime;
	/*
	 * 小件员id
	 */
	private int delivermanId;
	/*
	 * 小件员姓名
	 */
	private String delivermanName;
	/*
	 * 分配小件员的操作人id
	 */
	private int distributeUserId;
	/*
	 * 分配小件员的操作人姓名
	 */
	private String distributeUserName;
	/*
	 * 快递单号（也是订单表中的订单号）
	 */
	private String orderNo;
	/*
	 * 一级原因id
	 */
	private int feedbackFirstReasonId;
	/*
	 * 一级原因
	 */
	private String feedbackFirstReason;
	/*
	 * 二级原因id
	 */
	private int feedbackSecondReasonId;
	/*
	 * 二级原因
	 */
	private String feedbackSecondReason;
	/*
	 * 反馈备注
	 */
	private String feedbackRemark;
	/*
	 * 反馈人id
	 */
	private int feedbackUserId;
	/*
	 * 反馈人姓名
	 */
	private String feedbackUserName;
	/*
	 * 反馈时间
	 */
	private Date feedbackTime;

	/*
	 * 预计下次揽件时间
	 */
	private Date nextPickTime;

	/*
	 * 托物资料-其他
	 */
	private String other;
	/*
	 * 返回tps失败标识，0：成功，1：失败
	 */
	private int returnTpsFalseFlag;

	public ExpressPreOrder() {
		super();
	}

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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getExcuteState() {
		return this.excuteState;
	}

	public void setExcuteState(int excuteState) {
		this.excuteState = excuteState;
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

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public int getHandleUserId() {
		return this.handleUserId;
	}

	public void setHandleUserId(int handleUserId) {
		this.handleUserId = handleUserId;
	}

	public String getHandleUserName() {
		return this.handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public Date getDistributeDelivermanTime() {
		return this.distributeDelivermanTime;
	}

	public void setDistributeDeliverman_time(Date distributeDelivermanTime) {
		this.distributeDelivermanTime = distributeDelivermanTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getArrangeTime() {
		return this.arrangeTime;
	}

	public void setArrangeTime(Date arrangeTime) {
		this.arrangeTime = arrangeTime;
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

	public int getDistributeUserId() {
		return this.distributeUserId;
	}

	public void setDistributeUserId(int distributeUserId) {
		this.distributeUserId = distributeUserId;
	}

	public String getDistributeUserName() {
		return this.distributeUserName;
	}

	public void setDistributeUserName(String distributeUserName) {
		this.distributeUserName = distributeUserName;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getFeedbackFirstReasonId() {
		return this.feedbackFirstReasonId;
	}

	public void setFeedbackFirstReasonId(int feedbackFirstReasonId) {
		this.feedbackFirstReasonId = feedbackFirstReasonId;
	}

	public String getFeedbackFirstReason() {
		return this.feedbackFirstReason;
	}

	public void setFeedbackFirstReason(String feedbackFirstReason) {
		this.feedbackFirstReason = feedbackFirstReason;
	}

	public int getFeedbackSecondReasonId() {
		return this.feedbackSecondReasonId;
	}

	public void setFeedbackSecondReasonId(int feedbackSecondReasonId) {
		this.feedbackSecondReasonId = feedbackSecondReasonId;
	}

	public String getFeedbackSecondReason() {
		return this.feedbackSecondReason;
	}

	public void setFeedbackSecondReason(String feedbackSecondReason) {
		this.feedbackSecondReason = feedbackSecondReason;
	}

	public String getFeedbackRemark() {
		return this.feedbackRemark;
	}

	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}

	public int getFeedbackUserId() {
		return this.feedbackUserId;
	}

	public void setFeedbackUserId(int feedbackUserId) {
		this.feedbackUserId = feedbackUserId;
	}

	public String getFeedbackUserName() {
		return this.feedbackUserName;
	}

	public void setFeedbackUserName(String feedbackUserName) {
		this.feedbackUserName = feedbackUserName;
	}

	public Date getFeedbackTime() {
		return this.feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public Date getNextPickTime() {
		return this.nextPickTime;
	}

	public void setNextPickTime(Date nextPickTime) {
		this.nextPickTime = nextPickTime;
	}

	public void setDistributeDelivermanTime(Date distributeDelivermanTime) {
		this.distributeDelivermanTime = distributeDelivermanTime;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public int getReturnTpsFalseFlag() {
		return this.returnTpsFalseFlag;
	}

	public void setReturnTpsFalseFlag(int returnTpsFalseFlag) {
		this.returnTpsFalseFlag = returnTpsFalseFlag;
	}
	

	public String getExcuteStateStr() {
		return excuteStateStr;
	}

	public void setExcuteStateStr(String excuteStateStr) {
		this.excuteStateStr = excuteStateStr;
	}


}
