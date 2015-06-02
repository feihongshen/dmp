package cn.explink.enumutil;

public enum ReturnCwbsTypeEnum {
	DaiFanDanChuZhan(0, "待返单出站"), FanDanRuKu(1, "返单入库"), FanDanChuZhan(2, "返单出站"),FanDanChuKu(3,"返单出库");

	private int value;
	private String text;

	private ReturnCwbsTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ReturnCwbsTypeEnum getText(int value) {
		for (ReturnCwbsTypeEnum typeEnum : ReturnCwbsTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}

	public static ReturnCwbsTypeEnum getText(long value) {
		for (ReturnCwbsTypeEnum typeEnum : ReturnCwbsTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
