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
	private String cwb;
	private String fileposition;
	private long lastdutybranchid;//最终审判机构
	private long lastdutyuserid;//最终审判的责任人 
	
	

	public long getLastdutybranchid() {
		return lastdutybranchid;
	}

	public void setLastdutybranchid(long lastdutybranchid) {
		this.lastdutybranchid = lastdutybranchid;
	}

	public long getLastdutyuserid() {
		return lastdutyuserid;
	}

	public void setLastdutyuserid(long lastdutyuserid) {
		this.lastdutyuserid = lastdutyuserid;
	}

	public String getFileposition() {
		return fileposition;
	}

	public void setFileposition(String fileposition) {
		this.fileposition = fileposition;
	}

	public long getAbnormalorderid() {
		return this.abnormalorderid;
	}

	public void setAbnormalorderid(long abnormalorderid) {
		this.abnormalorderid = abnormalorderid;
	}

	public long getAbnormalordertype() {
		return this.abnormalordertype;
	}

	public void setAbnormalordertype(long abnormalordertype) {
		this.abnormalordertype = abnormalordertype;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getCreuserid() {
		return this.creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCredatetime() {
		return this.credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public long getType() {
		return this.type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
