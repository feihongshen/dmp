package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递的排序字段枚举
 * 
 * @author jiangyu 2015年8月4日
 *
 */
public enum OrderFiledEnum {
	BillNo(1, "账单编号"), BillState(2, "账单状态"), CreateTime(3, "账单创建日期"), VerifyTime(4, "账单核销日期");

	private Integer value;

	private String text;

	private OrderFiledEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static OrderFiledEnum getByValue(Integer index) {
		for (OrderFiledEnum typeEnum : OrderFiledEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (OrderFiledEnum e : OrderFiledEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
