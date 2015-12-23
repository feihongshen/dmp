package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 账单类型
 * 0：客户运费，1：站点运费，2：跨省应收运费，3：跨省应付运费
 * @author jiangyu 2015年8月11日
 *
 */
public enum ExpressBillTypeEnum {

	CustomerFreightBill(0, "客户运费"), 
	StationFreightBill(1, "站点运费"), 
	AcrossProvinceReceivableBill(2, "跨省应收运费"), 
	AcrossProvincePayableBill(3, "跨省应付运费"), 
	AcrossProvinceCodReceivableBill(0, "跨省应收货款"), 
	AcrossProvinceCodPayableBill(1, "跨省应付货款");

	private Integer value;

	private String text;

	private ExpressBillTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ExpressBillTypeEnum getByValue(Integer index) {
		for (ExpressBillTypeEnum typeEnum : ExpressBillTypeEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressBillTypeEnum e : ExpressBillTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
