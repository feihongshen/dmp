package cn.explink.domain;

public class EmailDate {

	private long emaildateid;
	private String emaildatetime;
	long userid;
	long warehouseid;
	long customerid;
	private int state;

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getEmaildatetime() {
		return emaildatetime;
	}

	public void setEmaildatetime(String emaildatetime) {
		this.emaildatetime = emaildatetime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

}
