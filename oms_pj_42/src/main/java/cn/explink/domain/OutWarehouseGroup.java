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

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCredate() {
		return credate;
	}

	public void setCredate(Timestamp credate) {
		this.credate = credate;
	}

	public long getDriverid() {
		return driverid;
	}

	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}

	public long getTruckid() {
		return truckid;
	}

	public void setTruckid(long truckid) {
		this.truckid = truckid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPrinttime() {
		return printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}

	public long getOperatetype() {
		return operatetype;
	}

	public void setOperatetype(long operatetype) {
		this.operatetype = operatetype;
	}

}
