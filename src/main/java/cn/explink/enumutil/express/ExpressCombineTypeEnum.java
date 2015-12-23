package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 合包类型
 *
 * @author songkaojun 2015年11月2日
 */
public enum ExpressCombineTypeEnum {
	STATION_COMBINE(1, "站点合包"), WAREHOUSE_COMBINE(2, "库房合包");

	private Integer value;

	private String text;

	private ExpressCombineTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressCombineTypeEnum getByValue(Integer index) {
		for (ExpressCombineTypeEnum typeEnum : ExpressCombineTypeEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressCombineTypeEnum e : ExpressCombineTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
