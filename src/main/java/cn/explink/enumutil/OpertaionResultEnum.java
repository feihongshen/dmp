package cn.explink.enumutil;

public enum OpertaionResultEnum {
	Success(0), Fail(1), Continue(2);

	private int value;

	OpertaionResultEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static OpertaionResultEnum getEnum(long value) {
		for (OpertaionResultEnum typeEnum : OpertaionResultEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
