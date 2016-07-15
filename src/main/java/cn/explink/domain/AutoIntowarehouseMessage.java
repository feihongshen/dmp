/**
 * 
 */
package cn.explink.domain;

/**
 * 自动分拣线的消息记录
 * 
 * @author wangwei 2016年7月13日
 *  
 */
public class AutoIntowarehouseMessage extends BaseEntity{
	private long id;
	
	private String serialNo;
	
	private String cwb;
	
	private String sendContent;
	
	private String sendTime;
	
	private String receiveContent;
	
	private String receiveTime;
	
	private String handleStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceiveContent() {
		return receiveContent;
	}

	public void setReceiveContent(String receiveContent) {
		this.receiveContent = receiveContent;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

}
