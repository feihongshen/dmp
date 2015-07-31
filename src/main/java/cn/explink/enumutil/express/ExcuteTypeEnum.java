package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

public enum ExcuteTypeEnum {
	NotAllocatedStation(1, "未分配站点"), AllocatedStation(2, "已分配站点"), AllocatedDeliveryman(3, "已分配快递员"), DelayedEmbrace(4, "延迟揽件"), fail(5, "失败"), WrongOrSuperzone(6, "错发/超区"), Succeed(7, "成功");

	private long value;
	private String text;

	private ExcuteTypeEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<ExcuteTypeEnum> getAllStatus() {
		List<ExcuteTypeEnum> excuteTypeEnum = new ArrayList<ExcuteTypeEnum>();
		excuteTypeEnum.add(NotAllocatedStation);
		excuteTypeEnum.add(AllocatedStation);
		excuteTypeEnum.add(AllocatedDeliveryman);
		excuteTypeEnum.add(DelayedEmbrace);
		excuteTypeEnum.add(fail);
		excuteTypeEnum.add(WrongOrSuperzone);
		excuteTypeEnum.add(Succeed);
		return excuteTypeEnum;
	}

	public static String getTextByValue(int value) {
		List<ExcuteTypeEnum> allStatus = ExcuteTypeEnum.getAllStatus();
		for (ExcuteTypeEnum excuteTypeEnum : allStatus) {
			if (excuteTypeEnum.getValue() == value) {
				return excuteTypeEnum.getText();
			}
		}
		return "";
	}
}
