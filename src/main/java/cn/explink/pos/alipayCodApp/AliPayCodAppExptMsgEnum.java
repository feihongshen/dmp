package cn.explink.pos.alipayCodApp;

public enum AliPayCodAppExptMsgEnum {

	Success("00", "成功"), SignValidateFailed("01", "签名验证失败"), WangLuoYiChang("02", "网络异常"),

	;

	private String resp_code;
	private String resp_msg;

	private AliPayCodAppExptMsgEnum(String resp_code, String resp_msg) {
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;

	}
}
