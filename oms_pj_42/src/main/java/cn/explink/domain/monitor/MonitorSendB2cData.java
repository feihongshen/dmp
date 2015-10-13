package cn.explink.domain.monitor;

public class MonitorSendB2cData {
	private long id;
	private String requesttime;
	private long resultcount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(String requesttime) {
		this.requesttime = requesttime;
	}

	public long getResultcount() {
		return resultcount;
	}

	public void setResultcount(long resultcount) {
		this.resultcount = resultcount;
	}

}
