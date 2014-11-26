package cn.explink.enumutil;

public enum StockDetailEnum {

	ZhengChang(0, "正常"), Kui(1, "亏"), Ying(2, "赢");
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
