package cn.explink.domain;

public class BaleCwb {
	private long id;
	private long baleid;
	private String baleno;
	private String cwb;

	public BaleCwb() {
	}

	public BaleCwb(long id, long baleid, String baleno, String cwb) {
		super();
		this.id = id;
		this.baleid = baleid;
		this.baleno = baleno;
		this.cwb = cwb;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBaleid() {
		return baleid;
	}

	public void setBaleid(long baleid) {
		this.baleid = baleid;
	}

	public String getBaleno() {
		return baleno;
	}

	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
