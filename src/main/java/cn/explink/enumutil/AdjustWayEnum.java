package cn.explink.enumutil;
/**
 * 调整单创建的方式
 * @author jiangyu 2015年4月10日
 *
 */
public enum AdjustWayEnum {
	Forward(1,"正向"),
	Reverse(2,"逆向");
	
	private Integer value;
	private String text;

	private AdjustWayEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static AdjustWayEnum getByValue(int value) {
		for (AdjustWayEnum adjustWayEnum : AdjustWayEnum.values()) {
			if (value == adjustWayEnum.getValue()) {
				return adjustWayEnum;
			}
		}
		return AdjustWayEnum.Forward;
	}
}
