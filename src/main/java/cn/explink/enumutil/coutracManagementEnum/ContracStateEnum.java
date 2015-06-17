/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 合同状态
 * 
 * @author wangqiang
 */
public enum ContracStateEnum {
	xinjian(1, "新建"), zhixingzhong(2, "执行中"), hetongzhongzhi(3, "合同终止"), hetongjieshu(4, "合同结束");
	private int value;
	private String text;

	private ContracStateEnum(int value, String text) {
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
