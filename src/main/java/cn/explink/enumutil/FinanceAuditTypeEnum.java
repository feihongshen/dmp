package cn.explink.enumutil;

public enum FinanceAuditTypeEnum {
	AnFaHuoShiJian(1, "按发货时间结算"), AnPeiSongJieGuo(2, "按配送结果结算"), TuiHuoKuanJieSuan_RuZhang(3, "退货款结算(入账)"), TuiHuoKuanJieSuan_ChuZhang(4, "退货款结算(出账)");

	private int value;
	private String text;

	private FinanceAuditTypeEnum(int value, String text) {
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
