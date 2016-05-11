package cn.explink.domain;

import java.sql.Timestamp;

public class OutWarehouseGroup {
	private long id;
	private Timestamp credate;
	private long driverid;
	private long truckid;
	private int state;
	private long branchid;
	private String printtime;
	private long operatetype;
	private long customerid;
	private long currentbranchid;
	private String cwbs;
	private long sign;
	private String baleno;
	private long baleid;
	
	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCredate() {
		return this.credate;
	}

	public void setCredate(Timestamp credate) {
		this.credate = credate;
	}

	public long getDriverid() {
		return this.driverid;
	}

	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}

	public long getTruckid() {
		return this.truckid;
	}

	public void setTruckid(long truckid) {
		this.truckid = truckid;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPrinttime() {
		return this.printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}

	public long getOperatetype() {
		return this.operatetype;
	}

	public void setOperatetype(long operatetype) {
		this.operatetype = operatetype;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getCurrentbranchid() {
		return this.currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public String getCwbs() {
		return this.cwbs;
	}

	public void setCwbs(String cwbs) {
		this.cwbs = cwbs;
	}

	public long getSign() {
		return this.sign;
	}

	public void setSign(long sign) {
		this.sign = sign;
	}

	/**
	 * @return the baleno
	 */
	public String getBaleno() {
		return this.baleno;
	}

	/**
	 * @param baleno the baleno to set
	 */
	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public long getBaleid() {
		return baleid;
	}

	public void setBaleid(long baleid) {
		this.baleid = baleid;
	}

}
