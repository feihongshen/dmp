package cn.explink.entity;

import cn.explink.core.pager.Pageable;

/**
 * 催件短信
 * 
 * @author gaoll
 *
 */
public class CsPushSms extends Pageable{
	
	private Long id;
	private String cwbOrderNo;
	private String workOrderNo;
	private Long complaintType;//ComplaintTypeEnum
	private String handler;
	private Long complianBranchId;
	private String complianUserName;
	private String sendTime;
	private String smsContent;
	private String mobileNo;
	
	/**
	 * 空参构造 
	 */
	public CsPushSms() {
		super();
	}
	
	/**
	 * 必填项构造
	 * @param cwbOrderNo
	 * @param workOrderNo
	 * @param complaintType
	 * @param handler
	 * @param sendTime
	 * @param smsContent
	 */
	public CsPushSms(String cwbOrderNo, String workOrderNo, Long complaintType,
			String handler, String sendTime, String smsContent, String mobileNo) {
		super();
		this.cwbOrderNo = cwbOrderNo;
		this.workOrderNo = workOrderNo;
		this.complaintType = complaintType;
		this.handler = handler;
		this.sendTime = sendTime;
		this.smsContent = smsContent;
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
	
	
}
