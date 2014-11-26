package cn.explink.enumutil;

public enum SmsSendManageEnum {
	ALL(-1, "全部"), SUCCESS(0, "成功"), Failure(1, "失败"), Sending(2, "发送中");
	private int value;
	private String text;

	private SmsSendManageEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static SmsSendManageEnum getText(int value) {
		for (SmsSendManageEnum typeEnum : SmsSendManageEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
