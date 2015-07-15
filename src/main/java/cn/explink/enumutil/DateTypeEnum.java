/**
 *
 */
package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangqiang
 */
public enum DateTypeEnum {
	FanKuiRiQi(1, "反馈日期"), FaHuoRiQi(2, "发货日期"), RuKuRiQi(3, "入库日期");

	private int value;
	private String text;

	private DateTypeEnum(int value, String text) {
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
		for (DateTypeEnum typeEnum : DateTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (DateTypeEnum e : DateTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	public static Map<Long, String> getStateMap() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (DateTypeEnum e : DateTypeEnum.values()) {
			map.put(new Long(e.value), e.text);
		}
		return map;
	}
}
