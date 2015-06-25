/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 合同状态
 *
 * @author wangqiang
 */
public enum ContractStateEnum {
	XinJian(1, "新建"), ZhiXingZhong(2, "执行中"), HeTongZhongZhi(3, "合同中止"), HeTongJieShu(4, "合同结束");
	private int value;
	private String text;

	private ContractStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	
	public static ContractStateEnum getValue(long value) {
		for (ContractStateEnum typeEnum : ContractStateEnum
				.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
