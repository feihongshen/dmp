package cn.explink.b2c.tools;

public class CommonJoint {
	private long exptcodeid;
	private long reasonid;
	private long exptid;
	private String exptcode_remark;
	private long id;
	public long commonid;
	private String customercode;
	private String expt_code; // 异常编码
	private String expt_msg; // 异常原因
	public long expt_type;
	public String reasoncontent;
	public String commonname;
	public String reasontype;//异常类型，在广州通路开发时添加
	
	
	

	public String getReasontype() {
		return reasontype;
	}

	public void setReasontype(String reasontype) {
		this.reasontype = reasontype;
	}

	public String getCommonname() {
		return commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public String getReasoncontent() {
		return reasoncontent;
	}

	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCommonid() {
		return commonid;
	}

	public void setCommonid(long commonid) {
		this.commonid = commonid;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
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

	public long getExpt_type() {
		return expt_type;
	}

	public void setExpt_type(long expt_type) {
		this.expt_type = expt_type;
	}

}
