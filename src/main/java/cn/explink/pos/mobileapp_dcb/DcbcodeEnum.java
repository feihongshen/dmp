package cn.explink.pos.mobileapp_dcb;

public enum DcbcodeEnum {

	SUCCESS("000000", "成功"), SYSTEMERROR("100001", "系统内部错误"), YONGHUBUCUNZAI("100002", "用户不存在或手机号存在异常"), PASSWORDERROR("100003", "密码错误"), USERLOCK("100004", "用户被锁定"), CWBNOTFOUNT("200001",
			"反馈异常，单号不存在"), REPEATFEEDBACK("200002", "反馈异常，重复反馈"), ;

	private String code;
	private String text;

	private DcbcodeEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
