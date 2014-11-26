package cn.explink.b2c.huitongtx.addressmatch;

public enum MatchTypeEnum {
	WeiPiPei(0, "未匹配"), DiZhiKu(1, "自动匹配"), RenGong(2, "人工匹配");

	private int value;
	private String text;

	private MatchTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static MatchTypeEnum getText(int value) {
		for (MatchTypeEnum enums : MatchTypeEnum.values()) {
			if (enums.getValue() == value) {
				return enums;
			}
		}
		return null;
	}
}
