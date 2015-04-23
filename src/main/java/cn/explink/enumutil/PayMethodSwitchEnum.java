package cn.explink.enumutil;
/**
 * 是否修改支付方式标识
 * @author jiangyu 2015年4月13日
 *
 */
public enum PayMethodSwitchEnum {
	Yes(1,"修改过支付方式"),
	No(0,"没有修改过支付方式");
	
	private Integer value;
	private String text;

	private PayMethodSwitchEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static PayMethodSwitchEnum getByValue(int value) {
		for (PayMethodSwitchEnum payMethodSwitchEnum : PayMethodSwitchEnum.values()) {
			if (value == payMethodSwitchEnum.getValue()) {
				return payMethodSwitchEnum;
			}
		}
		return PayMethodSwitchEnum.No;
	}
}
