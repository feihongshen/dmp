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
	YiZhouLiangCi(1, "一周两次");
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
}
