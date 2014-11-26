package cn.explink.b2c.tools;

public class CommonReason {
	private long id;
	public long commonid;
	private String customercode;
	private String expt_code; // 异常编码
	private String expt_msg; // 异常原因
	public long expt_type;

	public long getExpt_type() {
		return expt_type;
	}

	public void setExpt_type(long expt_type) {
		this.expt_type = expt_type;
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

	public String getExpt_code() {
		return expt_code;
	}

	public void setExpt_code(String expt_code) {
		this.expt_code = expt_code;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public String getExpt_msg() {
		return expt_msg;
	}

	public void setExpt_msg(String expt_msg) {
		this.expt_msg = expt_msg;
	}

}
