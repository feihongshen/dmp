package cn.explink.enumutil;

public enum FrameEnumExcp {
	A1(0, "4"), A2(1, "4"), A3(2, "7"), A4(3, "9"), A5(4, "15"), A6(5, "6,46"), A7(6, "6"), A8(7, "40"), A9(8, "4"), A10(9, "6");

	private int value;
	private String text;

	private FrameEnumExcp(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static String getText(int value) {
		for (FrameEnumExcp typeEnum : FrameEnumExcp.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return "0";
	}

}
