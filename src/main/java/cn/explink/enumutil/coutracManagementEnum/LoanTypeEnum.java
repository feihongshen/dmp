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
	maibao(1, "买包"), qianshou(2, "签收");
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
}
