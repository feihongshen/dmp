package cn.explink.b2c.maisike;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum MaisikeStatusEmum {
	// 正向
	IN_STORE(FlowOrderTypeEnum.RuKu.getValue(), "IN_STORE"), // 入库
	OUT_DELIVERY(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "OUT_DELIVERY"), // 配送中。
	COMPLETE(FlowOrderTypeEnum.YiFanKui.getValue(), "COMPLETE"), // 配送完成
	RETURNED(FlowOrderTypeEnum.YiFanKui.getValue(), "RETURNED"), // 拒收

	// 逆向
	PICKUP_IN_STORE(FlowOrderTypeEnum.RuKu.getValue(), "PICKUP_IN_STORE"), // 确认取件-入库
	OUT_PICKUP(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "OUT_PICKUP"), // 取件中-派送中领货
	BACKED(FlowOrderTypeEnum.YiFanKui.getValue(), "BACKED"), // 退货完成
	REPLACEED(FlowOrderTypeEnum.YiFanKui.getValue(), "REPLACEED"), // 换货完成
	DENY_BACKED(FlowOrderTypeEnum.YiFanKui.getValue(), "DENY_BACKED"), // 已拒退

	OUT_BACKED(FlowOrderTypeEnum.EeJiZhanTuiHuoChuZhan.getValue(), "OUT_BACKED"), // 二级站退货出站

	;

	private int ownstatus;
	private String mskstatus;

	public int getOwnstatus() {
		return ownstatus;
	}

	public void setOwnstatus(int ownstatus) {
		this.ownstatus = ownstatus;
	}

	public String getMskstatus() {
		return mskstatus;
	}

	public void setMskstatus(String mskstatus) {
		this.mskstatus = mskstatus;
	}

	private MaisikeStatusEmum(int ownstatus, String mskstatus) {
		this.ownstatus = ownstatus;
		this.mskstatus = mskstatus;
	}
}
