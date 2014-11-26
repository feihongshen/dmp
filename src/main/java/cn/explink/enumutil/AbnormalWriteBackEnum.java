package cn.explink.enumutil;

public enum AbnormalWriteBackEnum {
	ChuangJian(1, "创建"), ChuLi(2, "处理"), HuiFu(3, "回复"), XiuGai(4, "修改");

	private int value;
	private String text;

	private AbnormalWriteBackEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public static String getTextByValue(long value) {
		String text = "";
		for (AbnormalWriteBackEnum ab : AbnormalWriteBackEnum.values()) {
			if (ab.getValue() == value) {
				text = ab.getText();
			}
		}
		return text;

	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
