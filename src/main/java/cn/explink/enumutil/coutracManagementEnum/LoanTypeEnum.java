/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 贷款类型
 *
 * @author wangqiang
 */
public enum LoanTypeEnum {
	MaiBao(1, "买包"), QianShou(2, "签收");
	private int value;
	private String text;

	private LoanTypeEnum(int value, String text) {
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
		for (LoanTypeEnum typeEnum : LoanTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
}
