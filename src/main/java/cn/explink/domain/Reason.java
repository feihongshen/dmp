package cn.explink.domain;

public class Reason {

	private long reasonid;
	private String reasoncontent;
	private long reasontype;
	private int whichreason;
	private int parentid;

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
	public int getWhichreason() {
		return whichreason;
	}

	public void setWhichreason(int whichreason) {
		this.whichreason = whichreason;
	}

	public long getReasonid() {
		return reasonid;
	}

	public void setReasonid(long reasonid) {
		this.reasonid = reasonid;
	}

	public String getReasoncontent() {
		return reasoncontent;
	}

	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
	}

	public long getReasontype() {
		return reasontype;
	}

	public void setReasontype(long reasontype) {
		this.reasontype = reasontype;
	}

}
