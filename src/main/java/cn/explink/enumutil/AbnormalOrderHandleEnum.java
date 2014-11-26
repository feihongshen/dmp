package cn.explink.enumutil;

public enum AbnormalOrderHandleEnum {
	YiChuLi(1, "已处理"), WeiChuLi(2, "未处理");

	private int value;
	private String text;

	private AbnormalOrderHandleEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
