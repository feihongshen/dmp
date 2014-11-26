package cn.explink.pos.mobileapp_dcb;

public enum DcbPodresultEnum {

	PeisongChengGong("9", "配送成功"), JuShou("2", "拒收"), ZhiLiu("1", "滞留"),

	;

	private String code;
	private String text;

	private DcbPodresultEnum(String code, String text) {
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
