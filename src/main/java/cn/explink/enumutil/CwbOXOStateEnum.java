package cn.explink.enumutil;


public enum CwbOXOStateEnum {
	UnProcessed(0, "未处理"), Processing(1, "处理中"), Processed(2, "已处理");

	private int value;
	private String text;

	private CwbOXOStateEnum(int value, String text) {
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
