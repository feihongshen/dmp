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
	XinJian(1, "新建"), ZhiXingZhong(2, "执行中"), HeTongZhongZhi(3, "合同终止"), HeTongJieShu(4, "合同结束");
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
