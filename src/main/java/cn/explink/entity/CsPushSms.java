package cn.explink.entity;

import cn.explink.core.pager.Pageable;

/**
 * 催件短信
 * 
 * @author gaoll
 *
 */
public class CsPushSms extends Pageable{
	
	/*
	 * 主键
	 */
	private Long id;
	/*
	 * 订单号
	 */
	private String cwbOrderNo;
	/*
	 * 工单号
	 */
	private String workOrderNo;
	/*
	 * 工单类型
	 */
	private Long complaintType;//ComplaintTypeEnum
	/*
	 * 工单状态
	 */
	private Integer complaintState;//ComplaintStateEnum
	/*
	 * 工单受理人
	 */
	private String handler;
	/*
	 * 被投诉机构id
	 */
	private Long complianBranchId;
	/*
	 * 被投诉员工username
	 */
	private String complianUserName;
	/*
	 * 催件短信发送时间
	 */
	private String sendTime;
	/*
	 * 催件短信内容
	 */
	private String smsContent;
	/*
	 * 收件方类型：1：机构  2：派送员
	 */
	private Integer receiveType;
	/*
	 * 收件方 
	 */
	private Long receiveId;
	/*
	 * 催件短信收件手机号
	 */
	private String mobileNo;
	
	/**
	 * 空参构造 
	 */
	public CsPushSms() {
		super();
	}
	
	/**
	 * 
	 * @param cwbOrderNo
	 * @param workOrderNo
	 * @param complaintState
	 * @param handler
	 * @param sendTime
	 * @param smsContent
	 * @param complianBranchId
	 * @param complianUserName
	 * @param receiveId
	 * @param receiveType
	 * @param mobileNo
	 */
	public CsPushSms(String cwbOrderNo, String workOrderNo,Integer complaintState,
			String handler, String sendTime, String smsContent,Long complianBranchId,String complianUserName,Long receiveId,Integer receiveType,String mobileNo) {
		super();
		this.cwbOrderNo = cwbOrderNo;
		this.workOrderNo = workOrderNo;
		this.complaintState = complaintState;
		this.handler = handler;
		this.sendTime = sendTime;
		this.smsContent = smsContent;
		this.complianBranchId = complianBranchId;
		this.complianUserName = complianUserName;
		this.receiveId = receiveId;
		this.receiveType = receiveType;
		this.mobileNo = mobileNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCwbOrderNo() {
		return cwbOrderNo;
	}
	public void setCwbOrderNo(String cwbOrderNo) {
		this.cwbOrderNo = cwbOrderNo;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public Long getComplaintType() {
		return complaintType;
	}
	public void setComplaintType(Long complaintType) {
		this.complaintType = complaintType;
	}
	public Integer getComplaintState() {
		return complaintState;
	}

	public void setComplaintState(Integer complaintState) {
		this.complaintState = complaintState;
	}
	public Integer getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public Long getComplianBranchId() {
		return complianBranchId;
	}
	public void setComplianBranchId(Long complianBranchId) {
		this.complianBranchId = complianBranchId;
	}
	public String getComplianUserName() {
		return complianUserName;
	}
	public void setComplianUserName(String complianUserName) {
		this.complianUserName = complianUserName;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Long getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(Long receiveId) {
		this.receiveId = receiveId;
	}
	
}
