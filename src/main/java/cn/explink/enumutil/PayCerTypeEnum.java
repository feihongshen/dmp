package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

public enum PayCerTypeEnum {
	shengfenzheng(1, "身份证"), huzhao(2, "护照"), junguanzheng(3, "军官证"), shibingzheng(4, "士兵证"), huixiangzheng(5, "回乡证"), linshishengfenzheng(6, "临时身份证"), hukoupu(7, "户口簿"), jingguanzheng(8, "警官证"), taibaozheng(
			9, "台胞证"), yingyezhizhao(10, "营业执照"), qitazhengjian(11, "其它证件"), ;
	private int value;
	private String text;

	private PayCerTypeEnum(int value, String text) {
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
	public static List<PayCerTypeEnum> getAllStatus() {
		List<PayCerTypeEnum> list = new ArrayList<PayCerTypeEnum>();
		list.add(shengfenzheng);
		list.add(huzhao);
		list.add(junguanzheng);
		list.add(shibingzheng);
		list.add(huixiangzheng);
		list.add(linshishengfenzheng);
		list.add(hukoupu);
		list.add(jingguanzheng);
		list.add(taibaozheng);
		list.add(yingyezhizhao);
		list.add(qitazhengjian);
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
		List<PayCerTypeEnum> allStatusList = PayCerTypeEnum.getAllStatus();
		for (PayCerTypeEnum status : allStatusList) {
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
	public static PayCerTypeEnum getEnumByValue(int value) {
		List<PayCerTypeEnum> allStatusList = PayCerTypeEnum.getAllStatus();
		for (PayCerTypeEnum status : allStatusList) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
}
