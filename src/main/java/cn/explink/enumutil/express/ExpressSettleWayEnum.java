package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;
/**
 * 快递的付款方式
 * @author jiangyu 2015年8月4日
 *
 */
public enum ExpressSettleWayEnum {
	NowPay(1, "现结"), 
	MonthPay(2, "月结"), 
	ArrivePay(3, "到付");

	private Integer value;

	private String text;

	private ExpressSettleWayEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressSettleWayEnum getByValue(Integer index) {
		for (ExpressSettleWayEnum typeEnum : ExpressSettleWayEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressSettleWayEnum e : ExpressSettleWayEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
