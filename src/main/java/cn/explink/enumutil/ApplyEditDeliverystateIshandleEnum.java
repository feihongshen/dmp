package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ApplyEditDeliverystateIshandleEnum {
	WeiChuLi(0, "未处理"), YiChuLi(1, "已处理");

	private int value;
	private String text;

	private ApplyEditDeliverystateIshandleEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ApplyEditDeliverystateIshandleEnum getByValue(int value) {
		for (ApplyEditDeliverystateIshandleEnum ct : ApplyEditDeliverystateIshandleEnum.values()) {
			if (ct.getValue() == value) {
				return ct;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.Invalid_Cwb_State);
	}
}
