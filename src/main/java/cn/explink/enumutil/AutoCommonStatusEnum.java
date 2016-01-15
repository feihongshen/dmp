package cn.explink.enumutil;

public enum AutoCommonStatusEnum {
	create(1, "待处理"), success(2, "处理成功"),fail(3, "处理失败");

	private int value;
	private String text;

	private AutoCommonStatusEnum(int value, String text) {
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
