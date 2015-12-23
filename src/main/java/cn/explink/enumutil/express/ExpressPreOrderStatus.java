package cn.explink.enumutil.express;
/**
 * 预订单状态枚举
 * @author wangzy
 *
 */
public enum ExpressPreOrderStatus {

	NORMAL(0,"正常"),CLOSEORDER(1,"关闭"),RETURNORDER(2,"退回");
	
	private int value;
	private String text;

	private ExpressPreOrderStatus(int value, String text) {
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
