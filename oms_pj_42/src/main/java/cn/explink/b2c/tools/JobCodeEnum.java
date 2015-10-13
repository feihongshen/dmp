package cn.explink.b2c.tools;

public enum JobCodeEnum {
	Success("00", "请求成功"), Error("01", "失败"), Exption("50", "系统异常");
	private String code;
	private String msg;

	private JobCodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
