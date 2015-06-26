/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 是否有押金
 *
 * @author wangqiang
 */
public enum WhetherHaveDepositEnum {
	You(1, "有押金"), MeiYou(2, "没押金");
	private int value;
	private String text;

	private WhetherHaveDepositEnum(int value, String text) {
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
		for (WhetherHaveDepositEnum typeEnum : WhetherHaveDepositEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
}
