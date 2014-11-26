package cn.explink.enumutil;

public enum DeliveridEnum {
	PaiSong(0, "派送中");
	private int value;
	private String text;

	private DeliveridEnum(int value, String text) {
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
