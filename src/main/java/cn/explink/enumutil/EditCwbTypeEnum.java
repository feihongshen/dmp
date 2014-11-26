package cn.explink.enumutil;

public enum EditCwbTypeEnum {
	ChongZhiShenHeZhuangTai(1, "重置审核状态"), XiuGaiJinE(2, "修改订单金额"), XiuGaiZhiFuFangShi(3, "修改支付方式"), XiuGaiDingDanLeiXing(4, "修改订单类型");

	private long value;
	private String text;

	private EditCwbTypeEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static EditCwbTypeEnum getByValue(long value) {
		for (EditCwbTypeEnum typeEnum : EditCwbTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
