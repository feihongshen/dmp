package cn.explink.enumutil;

public enum MpsTypeEnum {
	
	PuTong(0, "普通单"), YiPiaoDuoJian(1, "一票多件");

	private int value;
	private String text;

	private MpsTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static MpsTypeEnum getByValue(int value) {
		for (MpsTypeEnum typeEnum : MpsTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
