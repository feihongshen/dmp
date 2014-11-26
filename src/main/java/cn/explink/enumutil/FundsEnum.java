package cn.explink.enumutil;

/**
 * 货款结算 枚举
 * 
 * @author Administrator
 *
 */
public enum FundsEnum {
	Fahuo(1, "发货"), PeiSongChengGong(2, "配送成功"), TuiHuo(3, "退货");

	private long value;
	private String text;

	private FundsEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
