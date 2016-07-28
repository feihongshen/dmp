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
	
	private byte intowarehouseType;
	
	private String scancwb;
	
	private String cwb;
	
	private String baleno;
	
	private String entranceIP;
		
	private String sendContent;
	
	private String sendTime;
	
	private String receiveContent;
	
	private String receiveTime;
	
	private byte handleStatus;

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

	public byte getIntowarehouseType() {
		return intowarehouseType;
	}

	public void setIntowarehouseType(byte intowarehouseType) {
		this.intowarehouseType = intowarehouseType;
	}

	public String getScancwb() {
		return scancwb;
	}

	public void setScancwb(String scancwb) {
		this.scancwb = scancwb;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getBaleno() {
		return baleno;
	}

	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public String getEntranceIP() {
		return entranceIP;
	}

	public void setEntranceIP(String entranceIP) {
		this.entranceIP = entranceIP;
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

	public byte getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(byte handleStatus) {
		this.handleStatus = handleStatus;
	}

}
