package cn.explink.b2c.yihaodian;

public enum YiHaoDian_DeliveryStateEnum {
	PeiSongChengGong(1, "配送成功"), BuFenChengGong(2, "部分成功"), PeiSongShiBai(3, "配送失败"),

	;

	private int value;
	private String text;

	private YiHaoDian_DeliveryStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static YiHaoDian_DeliveryStateEnum getByValue(int value) {
		for (YiHaoDian_DeliveryStateEnum deliveryStateEnum : YiHaoDian_DeliveryStateEnum.values()) {
			if (value == deliveryStateEnum.getValue()) {
				return deliveryStateEnum;
			}
		}
		return null;
	}
}
