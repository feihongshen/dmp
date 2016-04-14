package cn.explink.enumutil;

public enum IsmpsflagEnum {
	no(0, "普通件"), yes(1, "一票多件"), ;

	private int value;
	private String text;

	private IsmpsflagEnum(int value, String text) {
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
