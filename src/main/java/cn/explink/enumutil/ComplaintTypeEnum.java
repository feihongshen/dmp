package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintTypeEnum {
	CuijianTousu(1, "催件投诉"), FuwuTousu(2, "服务投诉"), KefuBeizhu(3, "客服备注"),DingDanChaXun(4,"订单查询");

	private long value;
	private String text;	

	private ComplaintTypeEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ComplaintTypeEnum getByValue(long value) {
		for (ComplaintTypeEnum complaintEnum : ComplaintTypeEnum.values()) {
			if (value == complaintEnum.getValue()) {
				return complaintEnum;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.GongDanLeiXingWeiZhaoDao, value);
	}
}
