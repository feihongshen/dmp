package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递中导入的应付订单详情是否有效的标识
 * 
 * @author jiangyu 2015年8月4日
 * 0：无效，1：生效
 */
public enum ExpressEffectiveEnum {
	UnEffective(0, "无效"), 
	Effective(1, "生效"),
	other(2, "其他");

	private Integer value;

	private String text;

	private ExpressEffectiveEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressEffectiveEnum getByValue(Integer index) {
		for (ExpressEffectiveEnum typeEnum : ExpressEffectiveEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressEffectiveEnum e : ExpressEffectiveEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
