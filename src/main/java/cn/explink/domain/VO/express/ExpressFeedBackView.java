package cn.explink.domain.VO.express;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import cn.explink.domain.express.ExpressPreOrder;
/**
 * 快递反馈页面的字段的视图实体
 * @author jiangyu 2015年8月8日
 *
 */
public class ExpressFeedBackView {
	
	/*
	 * 主键  (not null)
	 */
	private int id;
	/*
	 * 预订单编号(not null)
	 */
	private String preOrderNo;
	/*
	 *预订单状态（0：正常，1：关闭，2：退回）
	 */
	private int status;
	/*
	 * 执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)
	 */
	private int excuteState;
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
	//标识是否是暂不处理
	private int gcaid;//【-1:暂不处理；0：未处理；1：已反馈】
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPreOrderNo() {
		return preOrderNo;
	}
	public void setPreOrderNo(String preOrderNo) {
		this.preOrderNo = preOrderNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getExcuteState() {
		return excuteState;
	}
	public void setExcuteState(int excuteState) {
		this.excuteState = excuteState;
	}
	public String getSendPerson() {
		return sendPerson==null?"":sendPerson;
	}
	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}
	public String getCellphone() {
		return cellphone==null?"":cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getTelephone() {
		return telephone==null?"":telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCollectAddress() {
		return collectAddress==null?"":collectAddress;
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
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public int getHandleUserId() {
		return handleUserId;
	}
	public void setHandleUserId(int handleUserId) {
		this.handleUserId = handleUserId;
	}
	public String getHandleUserName() {
		return handleUserName;
	}
	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}
	public Date getDistributeDelivermanTime() {
		return distributeDelivermanTime;
	}
	public String getDistributeDelivermanTimeStr() {
		if (distributeDelivermanTime!=null) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.distributeDelivermanTime);
		}else {
			return "";
		}
	}
	public void setDistributeDelivermanTime(Date distributeDelivermanTime) {
		this.distributeDelivermanTime = distributeDelivermanTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getArrangeTime() {
		return arrangeTime;
	}
	public void setArrangeTime(Date arrangeTime) {
		this.arrangeTime = arrangeTime;
	}
	public int getDelivermanId() {
		return delivermanId;
	}
	public void setDelivermanId(int delivermanId) {
		this.delivermanId = delivermanId;
	}
	public String getDelivermanName() {
		return delivermanName;
	}
	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}
	public int getDistributeUserId() {
		return distributeUserId;
	}
	public void setDistributeUserId(int distributeUserId) {
		this.distributeUserId = distributeUserId;
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
	public int getFeedbackFirstReasonId() {
		return feedbackFirstReasonId;
	}
	public void setFeedbackFirstReasonId(int feedbackFirstReasonId) {
		this.feedbackFirstReasonId = feedbackFirstReasonId;
	}
	public String getFeedbackFirstReason() {
		return feedbackFirstReason;
	}
	public void setFeedbackFirstReason(String feedbackFirstReason) {
		this.feedbackFirstReason = feedbackFirstReason;
	}
	public int getFeedbackSecondReasonId() {
		return feedbackSecondReasonId;
	}
	public void setFeedbackSecondReasonId(int feedbackSecondReasonId) {
		this.feedbackSecondReasonId = feedbackSecondReasonId;
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
	public int getFeedbackUserId() {
		return feedbackUserId;
	}
	public void setFeedbackUserId(int feedbackUserId) {
		this.feedbackUserId = feedbackUserId;
	}
	public String getFeedbackUserName() {
		return feedbackUserName;
	}
	public void setFeedbackUserName(String feedbackUserName) {
		this.feedbackUserName = feedbackUserName;
	}
	public Date getFeedbackTime() {
		return feedbackTime;
	}
	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}
	public int getGcaid() {
		return gcaid;
	}
	public void setGcaid(int gcaid) {
		this.gcaid = gcaid;
	}
	
	public Date getNextPickTime() {
		return nextPickTime;
	}
	public void setNextPickTime(Date nextPickTime) {
		this.nextPickTime = nextPickTime;
	}
	
	public String isHistory() {
		if (!new SimpleDateFormat("yyyy-MM-dd").format(this.distributeDelivermanTime).substring(0, 10).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
			return "{历}";
		} else {
			return "";
		}
	}
	public ExpressFeedBackView() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 特殊的构造方法
	 * @param preOrder
	 */
	public static ExpressFeedBackView copyValue(ExpressPreOrder preOrder) {
		ExpressFeedBackView view = new ExpressFeedBackView();
		BeanUtils.copyProperties(preOrder, view);
		return view;
	}
}
