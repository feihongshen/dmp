package cn.explink.b2c.vipshop;

public enum VipGathercompEnum {
	//最后一箱:1最后一箱 ，0默认
	Default(0, "默认"), Last(1, "最后一箱");

	private int value;
	private String text;

	private VipGathercompEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static VipGathercompEnum getByValue(int value) {
		for (VipGathercompEnum typeEnum : VipGathercompEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
