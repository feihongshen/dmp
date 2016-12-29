package cn.explink.domain.dto;

/**
 * 传输pdadto
 * 
 * @author Administrator
 *
 */
public class ExpressIntoDto {
	/**
	 * 订单号
	 */
	private String cwb;
	/**
	 * 寄件人姓名
	 */
	private String sendername;
	/**
	 * 寄件人证件号
	 */
	private String senderid;
	/**
	 * 寄件人手机
	 */
	private String sendercellphone;
	/**
	 * 寄件人固话
	 */
	private String sendertelephone;
	/**
	 * 寄件人地址
	 */
	private String senderaddress;
	/**
	 * 实际重量（kg）
	 */
	private double realweight;
	/**
	 * 揽件时间
	 */
	private String pickTime;
	/**
	 * 备注
	 */
	private String cwbremark;
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getSendername() {
		return sendername;
	}
	public void setSendername(String sendername) {
		this.sendername = sendername;
	}
	public String getSenderid() {
		return senderid;
	}
	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}
	public String getSendercellphone() {
		return sendercellphone;
	}
	public void setSendercellphone(String sendercellphone) {
		this.sendercellphone = sendercellphone;
	}
	public String getSendertelephone() {
		return sendertelephone;
	}
	public void setSendertelephone(String sendertelephone) {
		this.sendertelephone = sendertelephone;
	}
	public String getSenderaddress() {
		return senderaddress;
	}
	public void setSenderaddress(String senderaddress) {
		this.senderaddress = senderaddress;
	}
	public double getRealweight() {
		return realweight;
	}
	public void setRealweight(double realweight) {
		this.realweight = realweight;
	}
	public String getPickTime() {
		return pickTime;
	}
	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}
	public String getCwbremark() {
		return cwbremark;
	}
	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}
	
}
