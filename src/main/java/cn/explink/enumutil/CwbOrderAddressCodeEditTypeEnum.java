package cn.explink.enumutil;

public enum CwbOrderAddressCodeEditTypeEnum {
	WeiPiPei(0, "未匹配"), DiZhiKu(1, "地址库匹配"), XiuGai(2, "修改匹配"), RenGong(3, "人工匹配");

	private int value;
	private String text;

	private CwbOrderAddressCodeEditTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static CwbOrderAddressCodeEditTypeEnum getText(int value) {
		for (CwbOrderAddressCodeEditTypeEnum cwbOrderAddressCodeEditTypeEnum : CwbOrderAddressCodeEditTypeEnum.values()) {
			if (cwbOrderAddressCodeEditTypeEnum.getValue() == value) {
				return cwbOrderAddressCodeEditTypeEnum;
			}
		}
		return null;
	}
}
