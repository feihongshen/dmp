package cn.explink.enumutil;

public enum OrderResultTypeEnum {

	// 到货未派，已领货无结果，配送成功，部分退货，拒收，滞留，上门退成功，上门退失败，上门换成功，货物丢失
	FenZhanDaoHuoSaoMiao(7, "到货未派", "SubstationGoods", -1), FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货未派", "SubstationGoodsNoList", -1), FenZhanLingHuo(9, "已领货无结果", "ReceiveGoods", 0), PeiSongChengGong(
			18, "配送成功", "Success", 1), ShangMenTuiChengGong(19, "上门退成功", "BackToDoorSuccess", 2), ShangMenHuanChengGong(20, "上门换成功", "ChangeToDoorSuccess", 3), JuShou(21, "拒收", "ReturnGoods", 4), BuFenTuiHuo(
			22, "部分退货", "SomeReturnGoods", 5), FenZhanZhiLiu(23, "滞留", "StayGoods", 6), ShangMenJuTui(24, "上门退失败", "BackToDoorFail", 7), HuoWuDiuShi(25, "货物丢失", "GoodsLose", 8);

	private int value;
	private String text;
	private String method;
	private int dmpDeliveryStateEnumValue;// 匹配dmp的deliveryStateEnum的值匹配到oms中
											// -1为无对应

	private OrderResultTypeEnum(int value, String text, String method, int dmpDeliveryStateEnumValue) {
		this.value = value;
		this.text = text;
		this.method = method;
		this.dmpDeliveryStateEnumValue = dmpDeliveryStateEnumValue;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public String getMethod() {
		return method;
	}

	public int getDmpDeliveryStateEnumValue() {
		return dmpDeliveryStateEnumValue;
	}

	public static OrderResultTypeEnum getByDmpDeliveryStateEnumValue(int dmpDeliveryStateEnumValue) {
		for (OrderResultTypeEnum orderResultTypeEnum : OrderResultTypeEnum.values()) {
			if (dmpDeliveryStateEnumValue == orderResultTypeEnum.getDmpDeliveryStateEnumValue()) {
				return orderResultTypeEnum;
			}
		}
		return null;
	}
}
