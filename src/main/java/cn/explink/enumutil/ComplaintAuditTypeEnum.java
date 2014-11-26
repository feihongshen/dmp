package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintAuditTypeEnum {
	Weishenhe(0, "未审核"), Chenggong(1, "成立"), Shibai(2, "失败");

	private long value;
	private String text;

	private ComplaintAuditTypeEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ComplaintAuditTypeEnum getByValue(long value) {
		for (ComplaintAuditTypeEnum complaintEnum : ComplaintAuditTypeEnum.values()) {
			if (value == complaintEnum.getValue()) {
				return complaintEnum;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.PEi_SONG_LEI_XING_WEI_ZHAO_DAO, value);
	}
}
