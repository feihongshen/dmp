package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

public enum ExcuteStateEnum {
	NotAllocatedStation(0, "未分配站点"), AllocatedStation(1, "已分配站点"), AllocatedDeliveryman(2, "已分配快递员"), DelayedEmbrace(3, "延迟揽件"), fail(4, "失败"), StationSuperzone(5, "站点超区"), EmbraceSuperzone(6, "揽件超区"), Succeed(
			7, "成功");

	private int value;
	private String text;

	private ExcuteStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<ExcuteStateEnum> getAllStatus() {
		List<ExcuteStateEnum> excuteTypeEnum = new ArrayList<ExcuteStateEnum>();
		excuteTypeEnum.add(NotAllocatedStation);
		excuteTypeEnum.add(AllocatedStation);
		excuteTypeEnum.add(AllocatedDeliveryman);
		excuteTypeEnum.add(DelayedEmbrace);
		excuteTypeEnum.add(fail);
		excuteTypeEnum.add(StationSuperzone);
		excuteTypeEnum.add(EmbraceSuperzone);
		excuteTypeEnum.add(Succeed);
		return excuteTypeEnum;
	}

	public static String getTextByValue(int value) {
		List<ExcuteStateEnum> allStatus = ExcuteStateEnum.getAllStatus();
		for (ExcuteStateEnum excuteTypeEnum : allStatus) {
			if (excuteTypeEnum.getValue() == value) {
				return excuteTypeEnum.getText();
			}
		}
		return "";
	}
}
