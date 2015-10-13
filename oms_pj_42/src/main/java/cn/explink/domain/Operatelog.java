package cn.explink.domain;

public class Operatelog {

	private long id;
	private String operateman;
	private String operatetime;
	private String cwb;
	private String transcwb;
	private String operateremarks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOperateman() {
		return operateman;
	}

	public void setOperateman(String operateman) {
		this.operateman = operateman;
	}

	public String getOperatetime() {
		return operatetime;
	}

	public void setOperatetime(String operatetime) {
		this.operatetime = operatetime;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getOperateremarks() {
		return operateremarks;
	}

	public void setOperateremarks(String operateremarks) {
		this.operateremarks = operateremarks;
	}

}
