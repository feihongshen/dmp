package cn.explink.pos.yalian;

public enum YalianAppEnum {

	Success("000000", "正常"), xitongcuowu("100001", "系统内部错误"), usernull("100002", "用户不存在"), passwordwrong("100003", "密码错误"), suoding("100004", "用户被锁定"), Danhaonull("200001", "反馈异常，单号不存在"), Chongfu(
			"200002", "反馈异常，重复反馈"), Jushou("1001", "协议信息错误"), Xieyicuowu("1002", "协议信息错误"), Chaoshi("1003", "超时限（物流太慢）"), Shoujiposun("1004", "手机质量问题（物品破损）"), Haokabupei("1005", "与客户所选号卡不匹配"),

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

	private YalianAppEnum(String resp_code, String resp_msg) {
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;

	}
}
