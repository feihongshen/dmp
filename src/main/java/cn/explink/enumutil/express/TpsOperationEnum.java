package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

public enum TpsOperationEnum {
	EmbracedScan(1, "揽件扫描"), ArrivalScan(2, "进站扫描"), DepartureScan(3, "出站扫描"), PackScan(4, "打包扫描"), DeliveryScan(5, "派送扫描"), SignScan(6, "签收扫描"), StrandedScan(7, "滞留扫描"), ExceptScan(8, "异常扫描"), Rejection(
			9, "拒收"), TransferScan(10, "转站扫描");

	private int value;
	private String text;

	private TpsOperationEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<TpsOperationEnum> getAllStatus() {
		List<TpsOperationEnum> distributeConditionEnumList = new ArrayList<TpsOperationEnum>();
		distributeConditionEnumList.add(EmbracedScan);
		distributeConditionEnumList.add(ArrivalScan);
		distributeConditionEnumList.add(DepartureScan);
		distributeConditionEnumList.add(PackScan);
		distributeConditionEnumList.add(DeliveryScan);
		distributeConditionEnumList.add(SignScan);
		distributeConditionEnumList.add(StrandedScan);
		distributeConditionEnumList.add(ExceptScan);
		distributeConditionEnumList.add(Rejection);
		distributeConditionEnumList.add(TransferScan);
		return distributeConditionEnumList;
	}

	public static String getTextByValue(int value) {
		List<TpsOperationEnum> allStatusList = TpsOperationEnum.getAllStatus();
		for (TpsOperationEnum status : allStatusList) {
			if (status.getValue() == value) {
				return status.getText();
			}
		}
		return "";
	}

}
