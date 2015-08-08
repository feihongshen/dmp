package cn.explink.enumutil;

public enum CwbInBatchTypeEnum {
	KeYong(1,"可用"),BuKeYong(0,"不可用");
	
	private long value;
	private String text;

	private CwbInBatchTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
