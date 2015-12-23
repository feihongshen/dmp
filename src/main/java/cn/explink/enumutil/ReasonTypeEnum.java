package cn.explink.enumutil;

public enum ReasonTypeEnum {

	ChangeTrains(1, "中转原因"), BeHelpUp(2, "滞留原因"), ReturnGoods(3, "退货原因"), GiveResult(4, "配送结果备注"), RuKuBeiZhu(5, "入库备注"), TuiHuoZaiTou(6, "退货再投原因"), FuWuTouSuLeiXing(7, "服务投诉类型"),
	
	WeiShuaKa(8, "未刷卡原因"), DiuShi(9, "丢失原因"), WeiTuiShangPin(10, "未退商品原因"), TuiHuoZhanRuKuBeiZhu(11, "退货站入库备注"), ShiXiaoReason(12, "失效原因"), GongDanTouSuYuanYin(13, "工单投诉原因"), KeHuLeiXing(14, "客户类型"),

	PickFailed(15, "揽件失败原因"), WrongArea(16, "站点超区原因"), PickWrong(17, "揽件超区原因"), PickDelay(18, "延迟揽件原因");

	private int value;
	private String text;

	private ReasonTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static void main(String[] args) {

	}

}
