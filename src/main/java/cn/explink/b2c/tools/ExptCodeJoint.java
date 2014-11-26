package cn.explink.b2c.tools;

public class ExptCodeJoint {
	private long exptcodeid;
	private long reasonid;
	private long exptid;
	private String exptcode_remark;
	private long expt_type;
	private long support_key;
	private String expt_code;
	private String expt_msg;
	private String expt_remark;
	private String reasoncontent;
	private long customerid;
	private String customercode;

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getReasoncontent() {
		return reasoncontent;
	}

	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
	}

	public long getExpt_type() {
		return expt_type;
	}

	public void setExpt_type(long expt_type) {
		this.expt_type = expt_type;
	}

	public long getSupport_key() {
		return support_key;
	}

	public void setSupport_key(long support_key) {
		this.support_key = support_key;
	}

	public String getExpt_code() {
		return expt_code;
	}

	public void setExpt_code(String expt_code) {
		this.expt_code = expt_code;
	}

	public String getExpt_msg() {
		return expt_msg;
	}

	public void setExpt_msg(String expt_msg) {
		this.expt_msg = expt_msg;
	}

	public String getExpt_remark() {
		return expt_remark;
	}

	public void setExpt_remark(String expt_remark) {
		this.expt_remark = expt_remark;
	}

	public long getExptcodeid() {
		return exptcodeid;
	}

	public void setExptcodeid(long exptcodeid) {
		this.exptcodeid = exptcodeid;
	}

	public long getReasonid() {
		return reasonid;
	}

	public void setReasonid(long reasonid) {
		this.reasonid = reasonid;
	}

	public long getExptid() {
		return exptid;
	}

	public void setExptid(long exptid) {
		this.exptid = exptid;
	}

	public String getExptcode_remark() {
		return exptcode_remark;
	}

	public void setExptcode_remark(String exptcode_remark) {
		this.exptcode_remark = exptcode_remark;
	}
}
