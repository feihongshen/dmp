package cn.explink.domain;

import java.sql.Timestamp;

public class PrintcwbDetail {
	private long id;
	private Timestamp credate;
	private long userid;
	private String printdetail;
	private long operatetype;

	public PrintcwbDetail() {
	}

	public PrintcwbDetail(long id, long userid, Timestamp credate, String printdetail, int operatetype) {
		super();
		this.id = id;
		this.credate = credate;
		this.userid = userid;
		this.printdetail = printdetail;
		this.operatetype = operatetype;
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

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getPrintdetail() {
		return printdetail;
	}

	public void setPrintdetail(String printdetail) {
		this.printdetail = printdetail;
	}

	public long getOperatetype() {
		return operatetype;
	}

	public void setOperatetype(long operatetype) {
		this.operatetype = operatetype;
	}

}
