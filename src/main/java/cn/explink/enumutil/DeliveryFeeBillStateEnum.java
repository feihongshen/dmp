package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryFeeBillStateEnum {
	WeiShenHe(1, "未审核"), YiShenHe(2, "已审核"), YiHeXiao(3, "已核销");
	private int value;
	private String text;

	private DeliveryFeeBillStateEnum(int value, String text) {
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
		for (DeliveryFeeBillStateEnum typeEnum : DeliveryFeeBillStateEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (DeliveryFeeBillStateEnum e : DeliveryFeeBillStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
