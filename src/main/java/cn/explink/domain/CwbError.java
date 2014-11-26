package cn.explink.domain;

public class CwbError {
	private long id;
	private String cwb;
	private String cwbdetail;
	private int state;
	private String emaildate;
	private String message;

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

	public String getCwbdetail() {
		return cwbdetail;
	}

	public void setCwbdetail(String cwbdetail) {
		this.cwbdetail = cwbdetail;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
