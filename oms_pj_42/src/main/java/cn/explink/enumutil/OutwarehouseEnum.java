package cn.explink.enumutil;

public enum OutwarehouseEnum {
	KuFangChuKu(0, "库房出库"), TuiHuoChuKu(1, "退货出库"), ZhongZhuanChuKu(2, "中转出库");
	private int value;
	private String text;

	private OutwarehouseEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
