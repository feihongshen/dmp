package cn.explink.domain;

public class AbnormalWriteBack {
	private long id;
	private long opscwbid;
	private String describe;
	private long creuserid;
	private String credatetime;
	private long type;
	private long abnormalorderid;
	private long abnormalordertype;

	public long getAbnormalorderid() {
		return abnormalorderid;
	}

	public void setAbnormalorderid(long abnormalorderid) {
		this.abnormalorderid = abnormalorderid;
	}

	public long getAbnormalordertype() {
		return abnormalordertype;
	}

	public void setAbnormalordertype(long abnormalordertype) {
		this.abnormalordertype = abnormalordertype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getCreuserid() {
		return creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCredatetime() {
		return credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

}
