package cn.explink.enumutil;

public enum BalePDAEnum {
	OK(0, "000000", "成功", "success.wav"), 
	YI_CHANG_BAO_HAO(20, "200000", "异常包号，含有非法字符", "fail.wav"), 
	CHA_XUN_YI_CHANG_BAO_HAO_BU_CUN_ZAI(217, "200017", "无此包号", "fail.wav");

	private int value;
	private String code;
	private String error;
	private String vediourl;

	private BalePDAEnum(int value, String code, String error, String vediourl) {
		this.value = value;
		this.code = code;
		this.error = error;
		this.vediourl = vediourl;
	}

	public int getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}

	public String getError() {
		return error;
	}

	public String getVediourl() {
		return vediourl;
	}

}
