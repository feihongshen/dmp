package cn.explink.b2c.happygo;

public enum FunctionForHappy {
	HOPE000001(1, "HOPE000001"), HOPE000002(2, "HOPE000002"), HOPE000005(3, "HOPE000005"), HOPE000006(4, "HOPE000006"), HOPE000007(5, "HOPE000007"), HOPE000010(6, "HOPE000010"), HOPE000014(7,
			"HOPE000014"), HOPE000018(8, "HOPE000018"),

	HOPE000020(9, "HOPE000020"),

	TUIHUO(101, "退货"), JIAOHUAN(102, "交换"), JUSHOU(103, "拒收"), ZAOJUSHOU(104, "送货前拒收"),

	qq(41, "换货的出库"), ww(10, "正常配送的出库"), ee(30, "退货的回收"), rr(42, "换货的回收"),

	HuiShou(11, "pull"), ChuKu(12, "push");

	private int key;
	private String text;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private FunctionForHappy(int key, String text) {
		this.key = key;
		this.text = text;

	}
}
