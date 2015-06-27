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
	PuTongFaPiao(1, "普通发票"), ZengChiFaPiao(2, "增值税专用发票"), JiDaFaPiao(2, "机打发票"), DingEFaPiao(2, "定额发票");
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

	public static String getByValue(long value) {
		for (InvoiceTypeEnum typeEnum : InvoiceTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
}
