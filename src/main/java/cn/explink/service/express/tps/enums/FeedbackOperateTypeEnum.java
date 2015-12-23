package cn.explink.service.express.tps.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 操作类型对应状态
 *
 * 已下单 placeOrder 23 预约单《已下单》 DO<br>
 * 已分配省公司 matchCarrier 24 预约单《已分配省公司》 DO<br>
 * 已分配站点 distributionOrg 25 预约单《已分配站点》 DO<br>
 * 已分配快递员 distributionCourier 26 预约单《已分配快递员》 DO<br>
 * 揽件成功 receiveSuccess 27 预约单《揽件成功》 DO<br>
 * 省公司超区 provinceSurpass 28 预约单《省公司超区》 DO<br>
 * 站点超区 siteSurpass 29 预约单《站点超区》 DO<br>
 * 揽件超区 receiveSurpass 30 预约单《揽件超区》 DO<br>
 * 揽件失败 receiveFail 31 预约单《揽件失败》 DO<br>
 * 反馈滞留 ticklingDelay 32 预约单《反馈滞留》 DO<br>
 * 关闭 reserveClose 33 预约单《关闭》 DO<br>
 *
 *
 * @author songkaojun 2015年8月28日
 */
public enum FeedbackOperateTypeEnum {
	//added by jiangyu
	RegisteScan(0, "揽件扫描"), InboundScan(1, "进站扫描"), SorttingScan(2, "分拣完成扫描"), OutboundScan(3, "出站扫描"), PackingScan(4, "打包扫描"), UnpackScan(5, "拆包扫描"), WeightScan(6, "称重扫描"), LoadScan(7, "装车扫描"), UnLoadScan(
			8, "卸车扫描"), DeliveryScan(9, "派送扫描"), PickUpScan(10, "取件扫描"), SignInScan(11, "签收扫描"), ReturnScan(12, "返站扫描"), RetensionScan(13, "滞留扫描"), TransferScan(14, "转站扫描"), ExceptionScan(15, "异常扫描"), ReceiveScan(
			16, "收货扫描"), UnPackCheckScan(17, "复核扫描"), UnSorttingScan(18, "取消分拣"), SendScan(19, "发货扫描"), UnSendScan(20, "取消发货"), StartScan(21, "发车扫描"), UnStartScan(22, "取消发车"),
	// added by jiangyu end
	PlaceOrder(23, "已下单"), MatchCarrier(24, "已分配省公司"), DistributionOrg(25, "已分配站点"), DistributionCourier(26, "已分配快递员"), ReceiveSuccess(27, "揽件成功"), ProvinceSurpass(28, "省公司超区"), SiteSurpass(29,
			"站点超区 "), ReceiveSurpass(30, "揽件超区"), ReceiveFail(31, "揽件失败"), TicklingDelay(32, "反馈滞留"), ReserveClose(33, "关闭"), JuShou(34, "拒收");

	private int value;
	private String text;

	private FeedbackOperateTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public static String getTextByValue(int value) {
		List<FeedbackOperateTypeEnum> allStatus = FeedbackOperateTypeEnum.getAllStatus();
		for (FeedbackOperateTypeEnum expressBillState : allStatus) {
			if (expressBillState.getValue() == value) {
				return expressBillState.getText();
			}
		}
		return "";
	}

	public static List<FeedbackOperateTypeEnum> getAllStatus() {
		List<FeedbackOperateTypeEnum> expressBillStateEnum = new ArrayList<FeedbackOperateTypeEnum>();
		expressBillStateEnum.add(RegisteScan);
		expressBillStateEnum.add(InboundScan);
		expressBillStateEnum.add(SorttingScan);
		expressBillStateEnum.add(OutboundScan);
		expressBillStateEnum.add(PackingScan);
		expressBillStateEnum.add(UnpackScan);
		expressBillStateEnum.add(WeightScan);
		expressBillStateEnum.add(LoadScan);
		expressBillStateEnum.add(UnLoadScan);
		expressBillStateEnum.add(DeliveryScan);
		expressBillStateEnum.add(PickUpScan);
		expressBillStateEnum.add(SignInScan);
		expressBillStateEnum.add(ReturnScan);
		expressBillStateEnum.add(RetensionScan);
		expressBillStateEnum.add(TransferScan);
		expressBillStateEnum.add(ExceptionScan);
		expressBillStateEnum.add(ReceiveScan);
		expressBillStateEnum.add(UnPackCheckScan);
		expressBillStateEnum.add(UnSorttingScan);
		expressBillStateEnum.add(SendScan);
		expressBillStateEnum.add(UnSendScan);
		expressBillStateEnum.add(StartScan);
		expressBillStateEnum.add(UnStartScan);
		expressBillStateEnum.add(PlaceOrder);
		expressBillStateEnum.add(MatchCarrier);
		expressBillStateEnum.add(DistributionOrg);
		expressBillStateEnum.add(DistributionCourier);
		expressBillStateEnum.add(ReceiveSuccess);
		expressBillStateEnum.add(ProvinceSurpass);
		expressBillStateEnum.add(SiteSurpass);
		expressBillStateEnum.add(ReceiveSurpass);
		expressBillStateEnum.add(ReceiveFail);
		expressBillStateEnum.add(TicklingDelay);
		expressBillStateEnum.add(ReserveClose);
		expressBillStateEnum.add(JuShou);

		return expressBillStateEnum;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}
