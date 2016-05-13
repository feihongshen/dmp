package cn.explink.domain.VO;

import java.util.Date;

/**
 * 预约单Vo
 * @date 2016年5月13日 下午6:04:39
 */
public class ReserveOrderVo {
	
	/**
	 * ID
	 */
	private Integer omReserveOrderId;
	
	/**
	 * 预约单号
	 */
	private String reserveOrderNo;
	
	/**
	 * 预约时间
	 */
	private Date appointTime;
	
	/**
	 * 预约时间-字符
	 */
	private String appointTimeStr;
	
	/**
	 * 寄货人
	 */
	private String cnorName;
	
	/**
	 * 手机
	 */
	private String cnorMobile;
	
	/**
	 * 寄件电话
	 */
	private String cnorTel;
	
	/**
	 * 寄件地址
	 */
	private String cnorAddr;
	
	/**
	 * 预约上门时间
	 */
	private Date requireTime;
	
	/**
	 * 预约上门时间-字符
	 */
	private String requireTimeStr;
	
	/**
	 * 预约单状态
	 */
	private Byte reserveOrderStatus;
	
	/**
	 * 预约单状态值
	 */
	private String reserveOrderStatusVal;
	
	/**
	 * 原因
	 */
	private String reason;
	
	/**
	 * 运单号
	 */
	private String transportNo;
	
	/**
	 * 揽件机构编号
	 */
	private String acceptOrg;
	
	/**
	* 揽件机构名称
	*/
	private String acceptOrgName;

	public Integer getOmReserveOrderId() {
		return omReserveOrderId;
	}

	public void setOmReserveOrderId(Integer omReserveOrderId) {
		this.omReserveOrderId = omReserveOrderId;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public String getAppointTimeStr() {
		return appointTimeStr;
	}

	public void setAppointTimeStr(String appointTimeStr) {
		this.appointTimeStr = appointTimeStr;
	}

	public String getCnorName() {
		return cnorName;
	}

	public void setCnorName(String cnorName) {
		this.cnorName = cnorName;
	}

	public String getCnorMobile() {
		return cnorMobile;
	}

	public void setCnorMobile(String cnorMobile) {
		this.cnorMobile = cnorMobile;
	}

	public String getCnorTel() {
		return cnorTel;
	}

	public void setCnorTel(String cnorTel) {
		this.cnorTel = cnorTel;
	}

	public String getCnorAddr() {
		return cnorAddr;
	}

	public void setCnorAddr(String cnorAddr) {
		this.cnorAddr = cnorAddr;
	}

	public String getRequireTimeStr() {
		return requireTimeStr;
	}

	public void setRequireTimeStr(String requireTimeStr) {
		this.requireTimeStr = requireTimeStr;
	}

	public Date getRequireTime() {
		return requireTime;
	}

	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}

	public Byte getReserveOrderStatus() {
		return reserveOrderStatus;
	}

	public void setReserveOrderStatus(Byte reserveOrderStatus) {
		this.reserveOrderStatus = reserveOrderStatus;
	}

	public String getReserveOrderStatusVal() {
		return reserveOrderStatusVal;
	}

	public void setReserveOrderStatusVal(String reserveOrderStatusVal) {
		this.reserveOrderStatusVal = reserveOrderStatusVal;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public String getAcceptOrg() {
		return acceptOrg;
	}

	public void setAcceptOrg(String acceptOrg) {
		this.acceptOrg = acceptOrg;
	}

	public String getAcceptOrgName() {
		return acceptOrgName;
	}

	public void setAcceptOrgName(String acceptOrgName) {
		this.acceptOrgName = acceptOrgName;
	}
}
