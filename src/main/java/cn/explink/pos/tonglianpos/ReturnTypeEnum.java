package cn.explink.pos.tonglianpos;

public enum ReturnTypeEnum {

	QuanTui(1, "全部退货"), BufenTui(2, "部分退货"), JuTui(3, "拒退")

	;

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResp_msg() {
		return this.resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	private int code;
	private String resp_msg;

	private ReturnTypeEnum(int code, String resp_msg) {
		this.code = code;
		this.resp_msg = resp_msg;

	}
}
