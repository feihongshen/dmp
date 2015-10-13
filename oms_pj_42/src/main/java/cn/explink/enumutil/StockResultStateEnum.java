package cn.explink.enumutil;

public enum StockResultStateEnum {
	ZhengZaiPanKu(1, "正在盘库状态"), PanKuJieShu(2, "盘库结束");
	private int value;
	private String text;

	private StockResultStateEnum(int value, String text) {
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
