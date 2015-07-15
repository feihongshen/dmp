/**
 *
 */
package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangqiang
 */
public enum OrderTypeEnum {
	SongHuo(1, "配送"), TuiHuo(2, "上门退"), HuanHuo(3, "上门换");

	private int value;
	private String text;

	private OrderTypeEnum(int value, String text) {
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
		for (OrderTypeEnum typeEnum : OrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (OrderTypeEnum e : OrderTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	public static Map<Long, String> getStateMap() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (OrderTypeEnum e : OrderTypeEnum.values()) {
			map.put(new Long(e.value), e.text);
		}
		return map;
	}
}
