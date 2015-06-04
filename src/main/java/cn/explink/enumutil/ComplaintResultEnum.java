package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintResultEnum {
	ChengLi(0,"成立"),
	BuChengLi(1,"不成立"),
	WeiChuLi(2,"未处理");
	
	private Integer value;
	private String text;

	private ComplaintResultEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	public static ComplaintResultEnum getByValue(long value) {
		for (ComplaintResultEnum cc : ComplaintResultEnum.values()) {
			if (value == cc.getValue()) {
				return cc;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.GongDanZhuangTaiWeiZhaoDao, value);
	}

}
