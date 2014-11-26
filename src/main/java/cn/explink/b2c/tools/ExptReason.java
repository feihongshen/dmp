package cn.explink.b2c.tools;

/**
 * b2c异常编码设置
 * 
 * @author Administrator
 *
 */
public class ExptReason {
	private long exptid;
	private long customerid;
	private String expt_code; // 异常编码
	private String expt_msg; // 异常原因
	private int expt_type; // 异常类型 （常用语设置枚举）
	private String expt_remark; // 备注
	private String customername; // b2c名称
	private String support_key; // 异常码提供方
	private String customercode; // 供货商编码

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public String getSupport_key() {
		return support_key;
	}

	public void setSupport_key(String support_key) {
		this.support_key = support_key;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public long getExptid() {
		return exptid;
	}

	public void setExptid(long exptid) {
		this.exptid = exptid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
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

	public int getExpt_type() {
		return expt_type;
	}

	public void setExpt_type(int expt_type) {
		this.expt_type = expt_type;
	}

	public String getExpt_remark() {
		return expt_remark;
	}

	public void setExpt_remark(String expt_remark) {
		this.expt_remark = expt_remark;
	}
}
