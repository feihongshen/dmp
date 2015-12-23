package cn.explink.domain.VO.express;

import java.util.ArrayList;
import java.util.List;

public enum DmpStepEnum {
	EmbracedFeedback(1, "揽件反馈"), EmbracedIn(2, "揽件入站"), EmbracedOut(3, "揽件出站"), Pack(4, "打包扫描"), DeliveryPicking(5, "小件员领货"), Sign(6, "签收"), Stranded(7, "站点/小件员滞留"), ToBeConverted(8, "待中转"), Rejection(
			9, "拒收"), TransferOut(10, "中转出站"), SortingArrival(11, "分拣库入库"), SortingDeparture(12, "分拣库出库"), StationArrival(13, "站点到货"), Missing(14, "丢失"), TransferArrival(15, "中转入库"), TransferDeparture(
			16, "中转出库");
	private int value;
	private String text;

	private DmpStepEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static List<DmpStepEnum> getAllStatus() {
		List<DmpStepEnum> distributeConditionEnumList = new ArrayList<DmpStepEnum>();
		distributeConditionEnumList.add(EmbracedFeedback);
		distributeConditionEnumList.add(EmbracedIn);
		distributeConditionEnumList.add(EmbracedOut);
		distributeConditionEnumList.add(Pack);
		distributeConditionEnumList.add(DeliveryPicking);
		distributeConditionEnumList.add(Sign);
		distributeConditionEnumList.add(Stranded);
		distributeConditionEnumList.add(ToBeConverted);
		distributeConditionEnumList.add(Rejection);
		distributeConditionEnumList.add(TransferOut);
		distributeConditionEnumList.add(SortingArrival);
		distributeConditionEnumList.add(SortingDeparture);
		distributeConditionEnumList.add(StationArrival);
		distributeConditionEnumList.add(Missing);
		distributeConditionEnumList.add(TransferArrival);
		distributeConditionEnumList.add(TransferDeparture);
		return distributeConditionEnumList;
	}

	public static String getTextByValue(int value) {
		List<DmpStepEnum> allStatusList = DmpStepEnum.getAllStatus();
		for (DmpStepEnum status : allStatusList) {
			if (status.getValue() == value) {
				return status.getText();
			}
		}
		return "";
	}

}
