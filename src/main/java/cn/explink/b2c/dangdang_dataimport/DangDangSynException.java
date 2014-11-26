package cn.explink.b2c.dangdang_dataimport;

public class DangDangSynException extends RuntimeException {

	private DangDangExptEnum error;

	public DangDangSynException(String expt_code, String expt_msg) {
		this.expt_code = expt_code;
		this.expt_msg = expt_msg;
	}

	public DangDangExptEnum getError() {
		return error;
	}

	private String expt_code;
	private String expt_msg;

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

}
