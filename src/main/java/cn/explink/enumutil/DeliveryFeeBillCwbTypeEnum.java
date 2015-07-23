package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryFeeBillCwbTypeEnum {
	QuanBu(0, "全部"), Song(1, "送"), Tui(2, "退"), Huan(3, "换");
	private int value;
	private String text;

	private DeliveryFeeBillCwbTypeEnum(int value, String text) {
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
		for (DeliveryFeeBillCwbTypeEnum typeEnum : DeliveryFeeBillCwbTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (DeliveryFeeBillCwbTypeEnum e : DeliveryFeeBillCwbTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
