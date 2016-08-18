package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

public enum CarTypeEnum {
	normal("普件", "普件"), big("大件", "大件");

	private String value;
	private String text;

	private CarTypeEnum(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	/**
	 * @Title: getAllType
	 * @description 获取枚举类中的所有值
	 * @author 刘武强
	 * @date  2016-07-25
	 * @param  @return
	 * @return  List<CarTypeEnum>
	 * @throws
	 */
	public static List<CarTypeEnum> getAllType() {
		List<CarTypeEnum> list = new ArrayList<CarTypeEnum>();
		list.add(normal);
		list.add(big);
		return list;
	}

	/**
	 * @Title: getAllStatus
	 * @description 通过枚举值，得到枚举描述
	 * @author 刘武强
	 * @date  2015年12月9日下午8:59:20
	 * @param  @return
	 * @return  List<TlResultEnum>
	 * @throws
	 */
	public static String getTextByValue(int value) {
		List<CarTypeEnum> allTypeList = CarTypeEnum.getAllType();
		for (CarTypeEnum status : allTypeList) {
			if (status.getValue().equals(value)) {
				return status.getText();
			}
		}
		return "";
	}

	/**
	 * @Title: getTextByValue
	 * @description 通过枚举值，得到枚举
	 * @author 刘武强
	 * @date  2015年12月9日下午8:59:40
	 * @param  @param value
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public static CarTypeEnum getEnumByValue(String value) {
		List<CarTypeEnum> allStatusList = CarTypeEnum.getAllType();
		for (CarTypeEnum status : allStatusList) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		return null;
	}
}
