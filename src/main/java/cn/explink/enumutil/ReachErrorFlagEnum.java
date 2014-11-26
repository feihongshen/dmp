package cn.explink.enumutil;

public enum ReachErrorFlagEnum {
	WeiDaoHuo(0, "未到货"), ZhengChangRuKu(1, "正常入库"), YouDanWuHuo(2, "有单无货"), YouHuoWuDan(3, "有货无单");

	private int value;
	private String text;

	private ReachErrorFlagEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
