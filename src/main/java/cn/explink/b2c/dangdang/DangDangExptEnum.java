package cn.explink.b2c.dangdang;

import cn.explink.enumutil.DeliveryStateEnum;

public enum DangDangExptEnum {
	CwbLost("06", "101", "订单丢失", "1", DeliveryStateEnum.JuShou.getValue()), CwbPoSun("06", "102", "订单破损", "2", DeliveryStateEnum.JuShou.getValue()), KeHuJuShou("06", "103", "客户拒收", "3",
			DeliveryStateEnum.JuShou.getValue()), AnPaiTuiHuo("06", "104", "安排退货", "4", DeliveryStateEnum.JuShou.getValue()), OutOfRange("06", "105", "超出配送范围", "5", DeliveryStateEnum.JuShou
			.getValue()), DangDangTuiHuo("06", "106", "当当要求退货", "6", DeliveryStateEnum.JuShou.getValue()), SearchNotFount("06", "107", "查无此人", "7", DeliveryStateEnum.JuShou.getValue()), AddressError(
			"06", "108", "地址不详/错误", "8", DeliveryStateEnum.JuShou.getValue()), OnContract("06", "109", "联系不上：多投无人，电话无人接听,关机", "9", DeliveryStateEnum.JuShou.getValue()),

	AgainDelivery("06", "201", "投递未遇安排再投", "2", DeliveryStateEnum.FenZhanZhiLiu.getValue()), AgainContract("06", "202", "联系不上收件人再联系", "3", DeliveryStateEnum.FenZhanZhiLiu.getValue()), UpdateAddress(
			"06", "203", "客户要求修改地址", "4", DeliveryStateEnum.FenZhanZhiLiu.getValue()), weekDayDelivey("06", "204", "客户要求双休日假日配送", "5", DeliveryStateEnum.FenZhanZhiLiu.getValue()), workDayDelivey(
			"06", "205", "客户要求工作日配送", "20", DeliveryStateEnum.FenZhanZhiLiu.getValue()), TuiChiPeiSongTime("06", "206", "客户要求推迟时间配送", "20", DeliveryStateEnum.FenZhanZhiLiu.getValue()), himSelfGoods(
			"06", "207", "客户要求自取", "7", DeliveryStateEnum.FenZhanZhiLiu.getValue()), RangService("06", "208", "超远地区，定期配送", "5", DeliveryStateEnum.FenZhanZhiLiu.getValue()), CwbLeaved("06", "209",
			"订单滞留", "20", DeliveryStateEnum.FenZhanZhiLiu.getValue()), WeatherReason("06", "210", "天气原因配送延缓", "2", DeliveryStateEnum.FenZhanZhiLiu.getValue()), OtherReason("06", "299", "其他原因", "20",
			DeliveryStateEnum.FenZhanZhiLiu.getValue());

	private String PosCode; // 支付宝设置当当的编码
	private String code; // 当当异常码
	private String exptReason; // 异常原因
	private String systemId; // 对应落地配 设置的原因Id
	private int DeliverState;// 异常原因类型。

	private DangDangExptEnum(String PosCode, String code, String exptReason, String systemId, int DeliverState) {
		this.PosCode = PosCode;
		this.code = code;
		this.exptReason = exptReason;
		this.systemId = systemId;
		this.DeliverState = DeliverState;
	}

	public int getDeliverState() {
		return DeliverState;
	}

	public void setDeliverState(int deliverState) {
		DeliverState = deliverState;
	}

	public String getPosCode() {
		return PosCode;
	}

	public void setPosCode(String posCode) {
		PosCode = posCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExptReason() {
		return exptReason;
	}

	public void setExptReason(String exptReason) {
		this.exptReason = exptReason;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
