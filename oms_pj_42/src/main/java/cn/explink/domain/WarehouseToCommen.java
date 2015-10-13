package cn.explink.domain;

public class WarehouseToCommen {

	private long id;
	private String cwb;
	private long customerid;
	private long startbranchid;
	private String commencode;
	private String credate;
	private String statetime;
	private long emaildateid;
	private long nextbranchid;
	private String remark;
	private int outbranchflag; // 标识环形对接出库类型， 0库房对库房 ，1 站对站，承运商出库确认按站点

	public int getOutbranchflag() {
		return outbranchflag;
	}

	public void setOutbranchflag(int outbranchflag) {
		this.outbranchflag = outbranchflag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

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

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public String getCommencode() {
		return commencode;
	}

	public void setCommencode(String commencode) {
		this.commencode = commencode;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

	public String getStatetime() {
		return statetime;
	}

	public void setStatetime(String statetime) {
		this.statetime = statetime;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

}
