package cn.explink.enumutil;


/**
 * 唯品会上门换业务换货标志（0：不是，1：是）
 *
 * @author songkaojun 2016年1月7日
 */
public enum VipExchangeFlagEnum {
	NO(0, "不是换货"), YES(1, "是换货");

	private int value;
	private String text;

	private VipExchangeFlagEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}
