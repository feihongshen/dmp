/**
 *
 */
package cn.explink.enumutil.coutracManagementEnum;

/**
 * 发票类型
 *
 * @author wangqiang
 */
public enum InvoiceTypeEnum {
	// 由于发票类型暂时未定，所以先这样显示
	FaPiaoLeiXing1(1, "1"), FaPiaoLeiXing2(2, "2");
	private int value;
	private String text;

	private InvoiceTypeEnum(int value, String text) {
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
