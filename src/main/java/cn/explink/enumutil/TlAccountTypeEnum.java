package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

public enum TlAccountTypeEnum {
	personal(0, "私人"), company(1, "企业");

	private int value;
	private String text;

	private TlAccountTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	/**
	 * @Title: getAllStatus
	 * @description 获取枚举类中的所有值
	 * @author 刘武强
	 * @date  2015年12月9日下午9:00:21
	 * @param  @return
	 * @return  List<TlResultEnum>
	 * @throws
	 */
	public static List<TlAccountTypeEnum> getAllStatus() {
		List<TlAccountTypeEnum> list = new ArrayList<TlAccountTypeEnum>();
		list.add(company);
		list.add(personal);
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
		List<TlAccountTypeEnum> allStatusList = TlAccountTypeEnum.getAllStatus();
		for (TlAccountTypeEnum status : allStatusList) {
			if (status.getValue() == value) {
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
	public static TlAccountTypeEnum getEnumByValue(int value) {
		List<TlAccountTypeEnum> allStatusList = TlAccountTypeEnum.getAllStatus();
		for (TlAccountTypeEnum status : allStatusList) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
}
