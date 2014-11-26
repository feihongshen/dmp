package cn.explink.enumutil;

import java.util.Date;

public enum ReasonTypeEnum {

	ChangeTrains(1, "中转原因"), BeHelpUp(2, "滞留原因"), ReturnGoods(3, "退货原因"), GiveResult(4, "配送结果备注"), RuKuBeiZhu(5, "入库备注"), TuiHuoZaiTou(6, "退货再投原因"), FuWuTouSuLeiXing(7, "服务投诉类型"), WeiShuaKa(8,
			"未刷卡原因"), DiuShi(9, "丢失原因"), WeiTuiShangPin(10, "未退商品原因");

	private int value;
	private String text;

	private ReasonTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static void main(String[] args) {

	}

}
