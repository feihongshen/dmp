package cn.explink.b2c.liantong;

/**
 * 
 * @author Administrator 联通接口对应表实体
 */
public class LiantongEntity {

	private long b2cid;
	private String cwb;
	private String transcwb;
	private String acceptTime;
	private String acceptAddress;
	private String acceptAction;
	private String acceptName;
	private String remark;
	private long flowordertype;

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getB2cid() {
		return b2cid;
	}

	public void setB2cid(long b2cid) {
		this.b2cid = b2cid;
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

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(String acceptAction) {
		this.acceptAction = acceptAction;
	}

	public String getAcceptName() {
		return acceptName;
	}

	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
