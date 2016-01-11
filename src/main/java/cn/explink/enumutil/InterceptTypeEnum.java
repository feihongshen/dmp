package cn.explink.enumutil;

public enum InterceptTypeEnum {
	posun(1, "丢失"), diushi(2, "破损"), tuihuo(3, "退货");

	private int value;
	private String text;

	private InterceptTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
}