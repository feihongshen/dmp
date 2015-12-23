package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;
/**
 * 快递的付款方式
 * @author jiangyu 2015年8月4日
 *
 */
public enum ExpressSettleWayEnum {
	MonthPay(0, "月结"),
	NowPay(1, "现付"), 
	ArrivePay(2, "到付"),
	other(1000,"");

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
			if (typeEnum.getValue().intValue() == index.intValue()) {
				return typeEnum;
			}
		}
		return ExpressSettleWayEnum.other;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressSettleWayEnum e : ExpressSettleWayEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
