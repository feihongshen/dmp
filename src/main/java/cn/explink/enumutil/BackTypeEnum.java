package cn.explink.enumutil;

public enum BackTypeEnum {
	TuiHuoChuZhan(1, "退货出站"), TuiGongHuoShangChuKu(2, "退供应商出库"), KuFangRuKu(3, "库房入库"), TuiHuoChuZhan24(4, "24H超期未到退货中心"), TuiHuoChuZhan72(5, "24H超期未到退货中心"), ;

	private int value;
	private String text;

	private BackTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static BackTypeEnum getValue(long value) {
		for (BackTypeEnum typeEnum : BackTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
