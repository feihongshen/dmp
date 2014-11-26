package cn.explink.b2c.saohuobang;

public enum Special {
	putong("0", "普通"), yisui("1", "易碎"), yetai("2", "液态"), huaxuepin("3", "化学品"), baisefenmo("4", "白色粉末状"), xiangyan("5", "香烟"),

	;
	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	private String resp_code;
	private String resp_msg;

	private Special(String resp_code, String resp_msg) {
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;

	}

}
