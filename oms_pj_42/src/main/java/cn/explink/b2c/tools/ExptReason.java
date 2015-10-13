package cn.explink.b2c.tools;

/**
 * b2c异常编码设置
 * 
 * @author Administrator
 *
 */
public class ExptReason {

	private String expt_code; // 异常编码
	private String expt_msg; // 异常原因
	private int expt_type; // 异常类型 （常用语设置枚举）
	private String reasonid;//广州通路对应本系统中的原因id
	

	

	public String getReasonid() {
		return reasonid;
	}

	public void setReasonid(String reasonid) {
		this.reasonid = reasonid;
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

	public static void main(String[] args) {
		System.out.println(new ExptReason().getExpt_code());
	}

}
