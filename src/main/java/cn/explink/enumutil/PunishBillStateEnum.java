/**
 *
 */
package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * 对内扣罚对外赔付账单状态
 *
 */
public enum PunishBillStateEnum {
	WeiShenHe(1, "未审核"), YiShenHe(2, "已审核"), YiHeXiao(3, "已核销");
	private int value;
	private String text;

	private PunishBillStateEnum(int value, String text) {
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
		for (PunishBillStateEnum typeEnum : PunishBillStateEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (PunishBillStateEnum e : PunishBillStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
	
	public static Map<Long, String> getStateMap() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (PunishBillStateEnum e : PunishBillStateEnum.values()) {
			map.put(new Long((long)e.value), e.text);
		}
		return map;
	}
}
