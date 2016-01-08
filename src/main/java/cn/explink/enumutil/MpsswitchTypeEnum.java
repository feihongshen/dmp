package cn.explink.enumutil;

public enum MpsswitchTypeEnum {
	
	WeiKaiQiJiDan(0, "未开启集单"), KuFangJiDan(1, "库房集单"), ZhanDianJiDan(3, "站点集单");

	private int value;
	private String text;

	private MpsswitchTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static MpsswitchTypeEnum getByValue(int value) {
		for (MpsswitchTypeEnum typeEnum : MpsswitchTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
