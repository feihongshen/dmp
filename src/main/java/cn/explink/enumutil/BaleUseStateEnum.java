package cn.explink.enumutil;

public enum BaleUseStateEnum {
	ZaiShiYong(0, "在使用"),KeChongYong(1, "可重用");

	private int value;
	private String text;

	private BaleUseStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static BaleUseStateEnum getValue(long value) {
		for (BaleUseStateEnum typeEnum : BaleUseStateEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
