package cn.explink.enumutil;

public enum CwbOrderTypeIdEnum {
	Weiqueding(-1, "未确定"), Peisong(1, "配送"), Shangmentui(2, "上门退"), Shangmenhuan(3, "上门换");

	private int value;
	private String text;

	private CwbOrderTypeIdEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static CwbOrderTypeIdEnum getByValue(int cwbordertypeid) {
		for (CwbOrderTypeIdEnum cotie : CwbOrderTypeIdEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
}
