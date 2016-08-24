package cn.explink.enumutil;

public enum EMSTraceDataEnum {
	weichuli(0, "未处理"), chulichenggong(1, "处理成功"), chulishibai(2, "处理失败");

	private int value;
	private String text;

	private EMSTraceDataEnum(int value, String text) {
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
		for (EMSTraceDataEnum ae : EMSTraceDataEnum.values()) {
			if (ae.getValue() == value) {
				return ae.getText();
			}
		}
		return "";
	}
}
