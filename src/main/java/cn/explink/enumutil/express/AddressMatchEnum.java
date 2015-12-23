package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @description 地址库匹配的调用者
 * @author 刘武强
 * @data 2015年9月17日
 */
public enum AddressMatchEnum {

	PreOrderMatch(0, "预订单自动匹配站点"), OrderEmbraceMatch(1, "订单补录匹配站点"), ExpressOutStation(2, "揽件出站匹配站点");

	private AddressMatchEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ExpressBillStateEnum getByValue(Integer index) {
		for (ExpressBillStateEnum typeEnum : ExpressBillStateEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	/**
	 *
	 * @Title: getAllStatus
	 * @description 获取枚举里面的所有状态
	 * @author 刘武强
	 * @date 2015年8月13日下午2:34:25
	 * @param @return
	 * @return List<AddressMatchEnum>
	 * @throws
	 */
	public static List<AddressMatchEnum> getAllStatus() {
		List<AddressMatchEnum> addressMatchEnum = new ArrayList<AddressMatchEnum>();
		addressMatchEnum.add(PreOrderMatch);
		addressMatchEnum.add(OrderEmbraceMatch);
		addressMatchEnum.add(ExpressOutStation);
		return addressMatchEnum;
	}

	/**
	 *
	 * @Title: getTextByValue
	 * @description 通过id获取text
	 * @author 刘武强
	 * @date 2015年8月14日上午11:36:29
	 * @param @param value
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getTextByValue(int value) {
		List<AddressMatchEnum> allStatus = AddressMatchEnum.getAllStatus();
		for (AddressMatchEnum expressBillState : allStatus) {
			if (expressBillState.getValue() == value) {
				return expressBillState.getText();
			}
		}
		return "";
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (AddressMatchEnum e : AddressMatchEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	private Integer value;

	private String text;

	public String getText() {
		return this.text;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
