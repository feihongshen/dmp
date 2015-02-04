package cn.explink.enumutil;

public enum AbnormalOrderHandleEnum {
	WeiChuLi(2, "未处理"), yichuli(1, "已处理");

	private int value;
	private String text;

	private AbnormalOrderHandleEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
}
