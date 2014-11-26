package cn.explink.enumutil;

public enum CwbDiuShiIsHandleEnum {
	WeiChuLi(0, "未处理"), YiChuLi(1, "已处理");

	private int value;
	private String text;

	private CwbDiuShiIsHandleEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static CwbDiuShiIsHandleEnum getByValue(int value) {
		for (CwbDiuShiIsHandleEnum deliveryStateEnum : CwbDiuShiIsHandleEnum.values()) {
			if (value == deliveryStateEnum.getValue()) {
				return deliveryStateEnum;
			}
		}
		return null;
	}
}
