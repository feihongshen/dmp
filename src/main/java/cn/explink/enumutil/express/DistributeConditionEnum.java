package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

/**
 * 分配情况枚举
 *
 * @author songkaojun 2015年8月3日
 */
public enum DistributeConditionEnum {
	NotDistribute(1, "未分配"), Distributed(2, "已分配");

	private int value;
	private String text;

	private DistributeConditionEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<DistributeConditionEnum> getAllStatus() {
		List<DistributeConditionEnum> distributeConditionEnumList = new ArrayList<DistributeConditionEnum>();
		distributeConditionEnumList.add(NotDistribute);
		distributeConditionEnumList.add(Distributed);
		return distributeConditionEnumList;
	}

	public static String getTextByValue(int value) {
		List<DistributeConditionEnum> allStatusList = DistributeConditionEnum.getAllStatus();
		for (DistributeConditionEnum status : allStatusList) {
			if (status.getValue() == value) {
				return status.getText();
			}
		}
		return "";
	}
}
