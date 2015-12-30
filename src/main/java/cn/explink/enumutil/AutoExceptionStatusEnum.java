package cn.explink.enumutil;

public enum AutoExceptionStatusEnum {
	xinjian(1, "新建异常"), yixiufu(2, "异常已经修复");

	private int value;
	private String text;

	private AutoExceptionStatusEnum(int value, String text) {
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
