package cn.explink.enumutil;

public enum DeliveryPercentTypeEnum {

	DaoZhanWeiLing(1, "到站未领"), ZhiLiu(2, "到站未领"), WeiGuiBan(3, "到站未领");
	private int value;
	private String text;

	private DeliveryPercentTypeEnum(int value, String text) {
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
