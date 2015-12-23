package cn.explink.domain.express;

import java.util.Date;

public class ExpressOutStationInfo {

	/**
	 * id
	 */
	private int id;
	/**
	 * 运单id
	 */
	private int cwbId;
	/**
	 * 运单号
	 */
	private String cwb;
	/**
	 * 出站站点id
	 */
	private int outstationBranchid;
	/**
	 * 出站站点名称
	 */
	private String outstationBranchName;
	/**
	 * 操作人id
	 */
	private int handerId;
	/**
	 * 操作人名称
	 */
	private String handerName;
	/**
	 * 出站时间
	 */
	private String outstationTime;
	/**
	 * 小件员id
	 */
	private int collectorid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCwbId() {
		return cwbId;
	}
	public void setCwbId(int cwbId) {
		this.cwbId = cwbId;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public int getOutstationBranchid() {
		return outstationBranchid;
	}
	public void setOutstationBranchid(int outstationBranchid) {
		this.outstationBranchid = outstationBranchid;
	}
	public String getOutstationBranchName() {
		return outstationBranchName;
	}
	public void setOutstationBranchName(String outstationBranchName) {
		this.outstationBranchName = outstationBranchName;
	}
	public int getHanderId() {
		return handerId;
	}
	public void setHanderId(int handerId) {
		this.handerId = handerId;
	}
	public String getHanderName() {
		return handerName;
	}
	public void setHanderName(String handerName) {
		this.handerName = handerName;
	}
	public String getOutstationTime() {
		return outstationTime;
	}
	public void setOutstationTime(String outstationTime) {
		this.outstationTime = outstationTime;
	}
	public int getCollectorid() {
		return collectorid;
	}
	public void setCollectorid(int collectorid) {
		this.collectorid = collectorid;
	}

	
}
