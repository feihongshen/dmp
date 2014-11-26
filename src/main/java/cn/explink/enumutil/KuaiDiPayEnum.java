package cn.explink.enumutil;

public enum KuaiDiPayEnum {
	JiFangFu(1, "寄方付"), ShouFangFu(2, "收方付"), DiSanFang(3, "第三方支付");

	private int value;
	private String text;

	private KuaiDiPayEnum(int value, String text) {

		this.value = value;
		this.text = text;

	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static KuaiDiPayEnum getText(int value) {
		for (KuaiDiPayEnum typeEnum : KuaiDiPayEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;

	}

}
