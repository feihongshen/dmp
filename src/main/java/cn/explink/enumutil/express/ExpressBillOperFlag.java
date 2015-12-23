package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作标识
 * 
 * @author jiangyu 2015年8月11日
 *
 */
public enum ExpressBillOperFlag {
	Customer(0, "客户"), 
	Station(1, "站点"),
	AcrossProvinceRece(2, "跨省应收"),
	AcrossProvincePay(3, "跨省应付");

	private ExpressBillOperFlag(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ExpressBillOperFlag getByValue(Integer index) {
		for (ExpressBillOperFlag typeEnum : ExpressBillOperFlag.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressBillOperFlag e : ExpressBillOperFlag.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	private Integer value;

	private String text;

	public String getText() {
		return text;
	}

	public Integer getValue() {
		return value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
