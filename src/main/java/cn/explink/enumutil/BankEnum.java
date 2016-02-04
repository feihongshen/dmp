package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

public enum BankEnum {
	Icbc("0102", "中国工商银行"), ABC("0103", "中国农业银行"), CMB("0308", "招商银行"), BOC("0104", "中国银行");

	private String value;
	private String text;

	private BankEnum(String value, String text) {
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
	 * @Title: getAllStatus
	 * @description 获取枚举类中的所有值
	 * @author 刘武强
	 * @date  2015年12月9日下午9:00:21
	 * @param  @return
	 * @return  List<TlResultEnum>
	 * @throws
	 */
	public static List<BankEnum> getAllStatus() {
		List<BankEnum> list = new ArrayList<BankEnum>();
		list.add(Icbc);
		list.add(ABC);
		list.add(BOC);
		list.add(CMB);
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
		List<BankEnum> allStatusList = BankEnum.getAllStatus();
		for (BankEnum status : allStatusList) {
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
	public static BankEnum getEnumByValue(String value) {
		List<BankEnum> allStatusList = BankEnum.getAllStatus();
		for (BankEnum status : allStatusList) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		return null;
	}
}
