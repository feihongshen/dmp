package cn.explink.enumutil;

public enum DeliveryTongjiEnum {

	LingHuo(1, "领货"), JinriYIfankui(2, "已反馈"), JinriWeifankui(3, "今日未反馈"), zuoriWeifankui(4, "昨日未反馈");
	private long value;
	private String text;

	private DeliveryTongjiEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
