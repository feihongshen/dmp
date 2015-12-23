package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 揽件出站的操作订单号还是包号的枚举
 * @author jiangyu 2015年8月5日
 *
 */
public enum ExpressOutStationFlagEnum {
	OrderNo(1,"订单号"),
	BaleNo(2,"包号");
	
	private Integer value;

	private String text;

	private ExpressOutStationFlagEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressOutStationFlagEnum getByValue(Integer index) {
		for (ExpressOutStationFlagEnum typeEnum : ExpressOutStationFlagEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressOutStationFlagEnum e : ExpressOutStationFlagEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
