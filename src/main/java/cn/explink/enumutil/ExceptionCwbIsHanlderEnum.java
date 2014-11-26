package cn.explink.enumutil;

public enum ExceptionCwbIsHanlderEnum {
	WeiChuLi(0, "未处理"), YiChuLi(1, "已处理");
	private int value;
	private String text;

	private ExceptionCwbIsHanlderEnum(int value, String text) {
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
