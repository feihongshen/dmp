package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递单支付方式
 * 
 * @author 刘武强  2016年9月03日
 *
 */
public enum ExpressPaymethodEnum {
	YueJie(0, "月结"),
	XianFu(1, "现付"), 
	DaoFu(2, "到付"),
	DiSanFangZhiFu(3, "第三方支付");

	private ExpressPaymethodEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ExpressPaymethodEnum getByValue(Integer index) {
		for (ExpressPaymethodEnum typeEnum : ExpressPaymethodEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressPaymethodEnum e : ExpressPaymethodEnum.values()) {
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
