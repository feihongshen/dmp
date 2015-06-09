package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintStateEnum {
	DaiChuLi(0, "待处理"),  YiJieAn(1, "已结案"),DaiHeShi(2,"待核实"),YiHeShi(3,"已核实"),
	YiJieShu(4,"已结束"),JieAnChongShenZhong(5,"结案重审中");

	private int value;
	private String text;

	private ComplaintStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ComplaintStateEnum getByValue(int value) {
		for (ComplaintStateEnum ct : ComplaintStateEnum.values()) {
			if (ct.getValue() == value) {
				return ct;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.GongDanZhuangTaiWeiZhaoDao);
	}
}
