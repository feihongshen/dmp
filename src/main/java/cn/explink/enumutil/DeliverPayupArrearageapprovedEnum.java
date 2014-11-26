package cn.explink.enumutil;

public enum DeliverPayupArrearageapprovedEnum {
	WeiShenHe(0, "未审核"), YiTongGuo(1, "已通过"), WeiTongGuo(2, "未通过");

	private int value;
	private String text;

	private DeliverPayupArrearageapprovedEnum(int value, String text) {
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
