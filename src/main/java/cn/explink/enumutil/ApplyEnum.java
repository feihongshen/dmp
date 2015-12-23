package cn.explink.enumutil;

public enum ApplyEnum {
	dingdanjinE(1, "订单金额修改"), zhifufangshi(2, "支付方式修改"), dingdanleixing(3, "订单类型修改"), kuaidiyunfeijine(4, "快递运费金额修改");

	private int value;
	private String text;

	private ApplyEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static String getTextByValue(int value) {
		for (ApplyEnum ae : ApplyEnum.values()) {
			if (ae.getValue() == value) {
				return ae.getText();
			}
		}
		return "";
	}
}
