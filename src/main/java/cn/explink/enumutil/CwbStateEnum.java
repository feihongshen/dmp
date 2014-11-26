package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum CwbStateEnum {
	WUShuju(0, "无数据"), PeiShong(1, "配送"), TuiHuo(2, "退货"), DiuShi(3, "丢失"), WuXiaoShuJu(4, "无效数据"), TuiGongYingShang(5, "退供货商"), ZhongZhuan(6, "中转");

	private int value;
	private String text;

	private CwbStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static CwbStateEnum getByValue(int value) {
		for (CwbStateEnum ct : CwbStateEnum.values()) {
			if (ct.getValue() == value) {
				return ct;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.Invalid_Cwb_State);
	}
}
