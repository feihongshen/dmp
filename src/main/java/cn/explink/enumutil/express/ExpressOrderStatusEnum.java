package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

public enum ExpressOrderStatusEnum {
	Normal(0, "正常"), Close(1, "关闭"), Feedback(2, "退回");

	private int value;
	private String text;

	private ExpressOrderStatusEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<ExpressOrderStatusEnum> getAllStatus() {
		List<ExpressOrderStatusEnum> expressOrderStatusEnum = new ArrayList<ExpressOrderStatusEnum>();
		expressOrderStatusEnum.add(Normal);
		expressOrderStatusEnum.add(Close);
		expressOrderStatusEnum.add(Feedback);
		return expressOrderStatusEnum;
	}

	public static String getTextByValue(int value) {
		List<ExpressOrderStatusEnum> allStatus = ExpressOrderStatusEnum.getAllStatus();
		for (ExpressOrderStatusEnum expressOrderStatusEnum : allStatus) {
			if (expressOrderStatusEnum.getValue() == value) {
				return expressOrderStatusEnum.getText();
			}
		}
		return "";
	}
}
