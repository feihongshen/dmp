package cn.explink.enumutil;

public enum PaytypeEnum {
	// ================= sitetype ================
	Xianjin(1, "现金"), Pos(2, "POS"), Zhipiao(3, "支票"), Qita(4, "其他"), CodPos(5, "支付宝COD扫码支付");

	private int value;
	private String text;

	private PaytypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static PaytypeEnum getByValue(int value) {
		for (PaytypeEnum paytypeEnum : PaytypeEnum.values()) {
			if (value == paytypeEnum.getValue()) {
				return paytypeEnum;
			}
		}
		return PaytypeEnum.Qita;
	}
}
