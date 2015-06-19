package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintResultEnum {
	WeiChuLi(0,"待定"),
	BuChengLi(1,"不成立"),
	ChengLi(2,"成立");
	
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
