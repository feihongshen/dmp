package cn.explink.enumutil;

public enum FinanceDeliverPayUpDetailTypeEnum {
	JiaoKuan(0, "交款"), TiaoZhang(1, "调账"), ChongZhiShenHe(2, "改单[重置审核状态]"), XiuGaiJinE(3, "改单[修改订单金额]"), XiuGaiZhiFuFangShi(4, "改单[修改支付方式]");

	private int value;
	private String text;

	private FinanceDeliverPayUpDetailTypeEnum(int value, String text) {
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
