package cn.explink.enumutil;

public enum YesOrNoStateEnum {
	Yes(1, "是"), No(0, "否");
	private int value;
	private String text;

	private YesOrNoStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	
	public static YesOrNoStateEnum getValue(long value) {
		for (YesOrNoStateEnum typeEnum : YesOrNoStateEnum
				.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
