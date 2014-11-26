package cn.explink.domain;

import java.sql.Timestamp;

public class ExceptionCwb {
	private long id;
	private String cwb;
	private long scantype;
	private String errortype;
	private Timestamp createtime;
	private long branchid;
	private long userid;
	private long ishanlder;
	private long customerid;
	private long driverid;
	private long truckid;
	private long deliverid;
	private String interfacetype;
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getScantype() {
		return scantype;
	}

	public void setScantype(long scantype) {
		this.scantype = scantype;
	}

	public String getErrortype() {
		return errortype;
	}

	public void setErrortype(String errortype) {
		this.errortype = errortype;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getIshanlder() {
		return ishanlder;
	}

	public void setIshanlder(long ishanlder) {
		this.ishanlder = ishanlder;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
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

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public String getInterfacetype() {
		return interfacetype;
	}

	public void setInterfacetype(String interfacetype) {
		this.interfacetype = interfacetype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
