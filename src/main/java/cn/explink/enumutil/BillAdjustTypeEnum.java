package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点调整账单-调整类型
 * @author eakel.liao
 *
 */
public enum BillAdjustTypeEnum {
	OrderFee(0, "货款调整"), ExpressFee(1, "运费调整");

	private int value;
	private String text;

	private BillAdjustTypeEnum(int value, String text) {
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
		for (BillAdjustTypeEnum typeEnum : BillAdjustTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.getText();
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (BillAdjustTypeEnum e : BillAdjustTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	public static Map<Long, String> getStateMap() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (BillAdjustTypeEnum e : BillAdjustTypeEnum.values()) {
			map.put(new Long(e.value), e.text);
		}
		return map;
	}
}
