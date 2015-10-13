package cn.explink.enumutil;

public enum ComplaintTypeEnum {

	Order(1, "订单"), Courier(2, "小件员"), Site(3, "站点");
	private int value;
	private String text;

	private ComplaintTypeEnum(int value, String text) {
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
