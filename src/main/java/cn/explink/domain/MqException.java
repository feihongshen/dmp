package cn.explink.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MQ异常model
 * @author jeef.fang
 *
 */
@Entity
@Table(name = "MQ_EXCEPTION")
public class MqException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "EXCEPTION_CODE")
	private String exceptionCode;
	
	@Column(name = "EXCEPTION_INFO")
	private String exceptionInfo;
	
	@Column(name = "TOPIC")
	private String topic;
	
	@Column(name = "MESSAGE_BODY")
	private String messageBody;

	@Column(name = "MESSAGE_HEADER")
	private String messageHeader;
	
	@Column(name = "HANDLE_COUNT")
	private int handleCount = 0;
	
	@Column(name = "HANDLE_FLAG")
	private boolean handleFlag = false;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "HANDLE_TIME")
	private Date handleTime;
	
	@Column(name = "MESSAGE_SOURCE")
	private String messageSource;
	
	@Column(name = "IS_AUTO_RESEND")
	private boolean isAutoResend = true;//默认自动重发
	
	@Column(name = "CREATED_BY_USER")
	private String createdByUser;
	@Column(name = "CREATED_OFFICE")
	private String createdOffice;
	
	@Column(name = "CREATED_DTM_LOC")
	private Date createdDtmLoc;
	
	@Column(name = "CREATED_TIME_ZONE")
	private String createdTimeZone;
	
	@Column(name = "UPDATED_BY_USER")
	private String updatedByUser;
	@Column(name = "UPDATED_OFFICE")
	private String updatedOffice;
	
	@Column(name = "UPDATED_DTM_LOC")
	private Date updatedDtmLoc;
	
	@Column(name = "UPDATED_TIME_ZONE")
	private String updatedTimeZone;
	
	@Column(name = "RECORD_VERSION")
	private int recordVersion = 0;
	
	@Column(name = "IS_DELETED")
	private boolean isDeleted = false;
	
	@Column(name = "ROUTEING_KEY")
	private String routeingKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public boolean isHandleFlag() {
		return handleFlag;
	}

	public void setHandleFlag(boolean handleFlag) {
		this.handleFlag = handleFlag;
	}
	
	public void setIsHandleFlag(boolean handleFlag) {
		this.handleFlag = handleFlag;
	}

	public String getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	public int getHandleCount() {
		return handleCount;
	}

	public void setHandleCount(int handleCount) {
		this.handleCount = handleCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getCreatedOffice() {
		return createdOffice;
	}

	public void setCreatedOffice(String createdOffice) {
		this.createdOffice = createdOffice;
	}

	public String getCreatedTimeZone() {
		return createdTimeZone;
	}

	public void setCreatedTimeZone(String createdTimeZone) {
		this.createdTimeZone = createdTimeZone;
	}

	public String getUpdatedByUser() {
		return updatedByUser;
	}

	public void setUpdatedByUser(String updatedByUser) {
		this.updatedByUser = updatedByUser;
	}

	public String getUpdatedOffice() {
		return updatedOffice;
	}

	public void setUpdatedOffice(String updatedOffice) {
		this.updatedOffice = updatedOffice;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public Date getCreatedDtmLoc() {
		return createdDtmLoc;
	}

	public void setCreatedDtmLoc(Date createdDtmLoc) {
		this.createdDtmLoc = createdDtmLoc;
	}

	public Date getUpdatedDtmLoc() {
		return updatedDtmLoc;
	}

	public void setUpdatedDtmLoc(Date updatedDtmLoc) {
		this.updatedDtmLoc = updatedDtmLoc;
	}

	public String getUpdatedTimeZone() {
		return updatedTimeZone;
	}

	public void setUpdatedTimeZone(String updatedTimeZone) {
		this.updatedTimeZone = updatedTimeZone;
	}

	public int getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(int recordVersion) {
		this.recordVersion = recordVersion;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRouteingKey() {
		return routeingKey;
	}

	public void setRouteingKey(String routeingKey) {
		this.routeingKey = routeingKey;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(String messageSource) {
		this.messageSource = messageSource;
	}

	public boolean isAutoResend() {
		return isAutoResend;
	}

	public void setAutoResend(boolean isAutoResend) {
		this.isAutoResend = isAutoResend;
	}
	
	public void setIsAutoResend(boolean isAutoResend) {
		this.isAutoResend = isAutoResend;
	}

}
