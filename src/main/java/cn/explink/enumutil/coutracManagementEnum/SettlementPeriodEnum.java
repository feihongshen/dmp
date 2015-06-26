/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 结算周期
 *
 * @author wangqiang
 */
public enum SettlementPeriodEnum {
	YiZhouYiCi(1, "一周一次"), YiZhouLiangCi(2, "一周两次");
	private int value;
	private String text;

	private SettlementPeriodEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static String getByValue(long value) {
		for (SettlementPeriodEnum typeEnum : SettlementPeriodEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
}
