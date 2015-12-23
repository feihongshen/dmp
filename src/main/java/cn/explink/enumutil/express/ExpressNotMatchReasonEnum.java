package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动核对的时候没有匹配的原因
 * 
 * @author jiangyu 2015年8月4日
 * 
 */
public enum ExpressNotMatchReasonEnum {
	TransNoNotExist(1, "运单号不存在"), 
	DeliveryFeeNotMatch(2, "运费合计不一致"),
	StationNotMatch(3, "站点不匹配");

	private Integer value;

	private String text;

	private ExpressNotMatchReasonEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressNotMatchReasonEnum getByValue(Integer index) {
		for (ExpressNotMatchReasonEnum typeEnum : ExpressNotMatchReasonEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressNotMatchReasonEnum e : ExpressNotMatchReasonEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
