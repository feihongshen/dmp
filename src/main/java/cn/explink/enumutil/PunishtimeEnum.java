package cn.explink.enumutil;

public enum PunishtimeEnum {
	Yiyue(1, "1"), Eryue(2, "2"), Sanyue(3, "3"), Siyue(4, "4"), Wuyue(5, "5"), Liuyue(6, "6"), Qiyue(7, "7"), Bayue(8, "8"), Jiuyue(9, "9"), Shiyue(10, "10"), Shiyiyue(11, "11"), Shieryue(12, "12");
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

	public static PunishtimeEnum getText(String name) {
		for (PunishtimeEnum pEnum : PunishtimeEnum.values()) {
			if (pEnum.getText().equals(name.trim())) {
				return pEnum;
			}
		}
		return null;
	}
}
