package cn.explink.enumutil;

public enum IsmpsflagEnum {
	no(0, "普通件"), yes(1, "一票多件"), ;

	private long value;
	private String text;

	private IsmpsflagEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
}
