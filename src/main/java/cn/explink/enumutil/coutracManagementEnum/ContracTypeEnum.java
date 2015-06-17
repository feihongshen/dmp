/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 合同类型
 * 
 * @author wangqiang
 */
public enum ContracTypeEnum {
	liangfang(1, "两方"), sanfang(2, "三方");
	private int value;
	private String text;

	private ContracTypeEnum(int value, String text) {
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
