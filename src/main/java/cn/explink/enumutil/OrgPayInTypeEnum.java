package cn.explink.enumutil;

public enum OrgPayInTypeEnum {
	StationPay(1, "站点缴款"), CourierPay(2, "小件员缴款");
	private int value;
	private String text;

	private OrgPayInTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static String getTextByValue(int value) {
		for (OrgPayInTypeEnum status : OrgPayInTypeEnum.values()) {
			if (status.getValue() == value) {
				return status.getText();
			}
		}
		return "";
	}

	public static OrgPayInTypeEnum getEnumByValue(int value) {
		for (OrgPayInTypeEnum status : OrgPayInTypeEnum.values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
}
