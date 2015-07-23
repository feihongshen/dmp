package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryFeeBillDateTypeEnum {
	ShenHeDate(1, "审核日期"), FanKuiDate(2, "反馈日期"), FaHuoDate(3, "发货日期"), RuKuDate(4, "入库日期");
	private int value;
	private String text;

	private DeliveryFeeBillDateTypeEnum(int value, String text) {
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
		for (DeliveryFeeBillDateTypeEnum typeEnum : DeliveryFeeBillDateTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (DeliveryFeeBillDateTypeEnum e : DeliveryFeeBillDateTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
