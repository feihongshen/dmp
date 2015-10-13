package cn.explink.domain;

import java.sql.Timestamp;


/**
 * 
 * 订单详情的快照类
 * 
 * @author jinghui.pan
 *
 */
public class CwbOrderSnapshot extends CwbOrder {
	
	/**
	 *  主键id
	 */
	private long id; 

	/**
	 * 生命周期报表id
	 */
	private long fnrptlifecycleid;
	
	private String cwbordertypeDesc;
	
	private String cwbstateDesc;
	
	private String flowordertypeDesc;
	
	private String currentbranchDesc;
	
	private String startbranchDesc;
	
	private String nextbranchDesc;
	
	private int reportdate;
	
	/**
	 * 生命周期报表生成标记：0未生成，1，已生成
	 */
	private int lifecycleRptFlag;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFnrptlifecycleid() {
		return fnrptlifecycleid;
	}

	public void setFnrptlifecycleid(long fnrptlifecycleid) {
		this.fnrptlifecycleid = fnrptlifecycleid;
	}

	public String getCwbordertypeDesc() {
		return cwbordertypeDesc;
	}

	public void setCwbordertypeDesc(String cwbordertypeDesc) {
		this.cwbordertypeDesc = cwbordertypeDesc;
	}

	public String getCwbstateDesc() {
		return cwbstateDesc;
	}

	public void setCwbstateDesc(String cwbstateDesc) {
		this.cwbstateDesc = cwbstateDesc;
	}

	public String getFlowordertypeDesc() {
		return flowordertypeDesc;
	}

	public void setFlowordertypeDesc(String flowordertypeDesc) {
		this.flowordertypeDesc = flowordertypeDesc;
	}

	public String getCurrentbranchDesc() {
		return currentbranchDesc;
	}

	public void setCurrentbranchDesc(String currentbranchDesc) {
		this.currentbranchDesc = currentbranchDesc;
	}

	public String getStartbranchDesc() {
		return startbranchDesc;
	}

	public void setStartbranchDesc(String startbranchDesc) {
		this.startbranchDesc = startbranchDesc;
	}

	public String getNextbranchDesc() {
		return nextbranchDesc;
	}

	public void setNextbranchDesc(String nextbranchDesc) {
		this.nextbranchDesc = nextbranchDesc;
	}

	public int getReportdate() {
		return reportdate;
	}

	public void setReportdate(int reportdate) {
		this.reportdate = reportdate;
	}

	public int getLifecycleRptFlag() {
		return lifecycleRptFlag;
	}

	public void setLifecycleRptFlag(int lifecycleRptFlag) {
		this.lifecycleRptFlag = lifecycleRptFlag;
	}


	
	
}
