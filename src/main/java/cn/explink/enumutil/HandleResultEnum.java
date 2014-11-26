package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum HandleResultEnum {
	JiaFangWeiFaHuo(1, "甲方未发货"), KuFangDiuShi(2, "库房丢失"), YunShuDiuShi(3, "运输丢失"), ZhanDianDiuShi(4, "站点丢失"), TuiHuoDiuShi(5, "退货丢失"), ZhongZhuanDiuShi(6, "中转丢失"), WuFaQueRenChuLiJieGuo(7, "无法确定处理结果");

	private int value;
	private String text;

	private HandleResultEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static HandleResultEnum getByValue(int value) {
		for (HandleResultEnum ct : HandleResultEnum.values()) {
			if (ct.getValue() == value) {
				return ct;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.Invalid_Cwb_State);
	}
}
