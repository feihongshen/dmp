package cn.explink.enumutil;

public enum PunishtimeEnum {
	Defalut(0, "默认"), Yitian(1, "一天");
	private long value;
	private String text;

	private PunishtimeEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static PunishtimeEnum getText(long value) {
		for (PunishtimeEnum pEnum : PunishtimeEnum.values()) {
			if (pEnum.getValue() == value) {
				return pEnum;
			}
		}
		return null;
	}
}
