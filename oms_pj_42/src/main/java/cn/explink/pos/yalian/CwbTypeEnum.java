package cn.explink.pos.yalian;

public enum CwbTypeEnum {
	Dianxin(1, "电信"), // 支付宝
	Yidong(2, "移动"), // 易宝支付
	Liantong(3, "联通"), // 银联
	;
	private int key;
	private String text;

	private CwbTypeEnum(int key, String text) {
		this.key = key;
		this.text = text;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
