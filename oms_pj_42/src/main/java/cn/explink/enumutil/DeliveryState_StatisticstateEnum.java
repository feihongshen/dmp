package cn.explink.enumutil;

public enum DeliveryState_StatisticstateEnum {
	WeiGuiBan(1, " 未归班"), YiGuiBan(2, "已归班"), ZanBuChuLi(3, "暂不处理");
	private int value;
	private String text;

	private DeliveryState_StatisticstateEnum(int value, String text) {
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
