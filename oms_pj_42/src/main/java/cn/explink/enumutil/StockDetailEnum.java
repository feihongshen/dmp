package cn.explink.enumutil;

public enum StockDetailEnum {

	ZhengChang(0, "正常"), YouHuoWuDan(1, "有货无单"), YouDanWuHuo(2, "有单无货");
	private int value;
	private String text;

	private StockDetailEnum(int value, String text) {
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
