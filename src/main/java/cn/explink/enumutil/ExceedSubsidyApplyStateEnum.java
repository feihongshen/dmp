package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum ExceedSubsidyApplyStateEnum {
	XinJian(1, "新建"), WeiShenHe(2, "未审核"), YiShenHe(3, "已审核"), YiJuJue(4, "已拒绝");
	private int value;
	private String text;

	private ExceedSubsidyApplyStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static String getByValue(long value) {
		for (ExceedSubsidyApplyStateEnum typeEnum : ExceedSubsidyApplyStateEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExceedSubsidyApplyStateEnum e : ExceedSubsidyApplyStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
