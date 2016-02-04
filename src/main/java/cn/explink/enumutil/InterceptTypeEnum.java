package cn.explink.enumutil;

public enum InterceptTypeEnum {//这个里面的枚举应该和运单状态枚举对应好，也就是说这里只是运单状态枚举的一部分
	diushi(1, "丢失"), posun(2, "破损"), tuihuo(4, "退货"), ;

	private int value;
	private String text;

	private InterceptTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	/**
	 *
	 * @Title: getTextByValue
	 * @description 通过value获取text
	 * @author 刘武强
	 * @date  2016年1月12日上午10:56:10
	 * @param  @param value
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public static String getTextByValue(int value) {
		for (InterceptTypeEnum temp : InterceptTypeEnum.values()) {
			if (temp.getValue() == value) {
				return temp.getText();
			}
		}
		return "";
	}
}