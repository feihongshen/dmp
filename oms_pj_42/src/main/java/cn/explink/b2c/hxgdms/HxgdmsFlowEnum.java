package cn.explink.b2c.hxgdms;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单流转信息推送
 * 
 * @author Administrator
 *
 */
public enum HxgdmsFlowEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "TMS_STATION_IN", "RequestState", "库房入库-分站进"), ChuKuSaoMiao(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "TMS_STATION_OUT", "RequestState", "库房出库-分站出"), FenZhanDaoHuoSaoMiao(
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "TMS_STATION_IN", "RequestState", "分站入站-分站进"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "TMS_DELIVERING",
			"RequestState", "领货-派送中"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "TMS_SIGN", "RequestState", "签收成功"), YiChang(FlowOrderTypeEnum.YiShenHe.getValue(), "TMS_ERROR", "RequestState",
			"异常"), // 拒收和滞留

	TuiHuoChuZhan(FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue(), "TMS_STATION_OUT", "RequestState", "退货出站"), TuihuoZhanRuKu(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "TMS_STATION_IN",
			"RequestState", "退货站入库-取件入仓"), // 拒收和滞留
	TuiGongYingShangChuKu(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "TMS_STATION_OUT", "RequestState", "退供货商出库"), // 拒收和滞留
	;

	private long state;
	private String dms_code;

	public String getDms_code() {
		return dms_code;
	}

	public void setDms_code(String dms_code) {
		this.dms_code = dms_code;
	}

	private String request_code; // 请求的指令

	private String text;

	private HxgdmsFlowEnum(long state, String dms_code, String request_code, String text) {
		this.state = state;
		this.dms_code = dms_code;
		this.request_code = request_code;
		this.text = text;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequest_code() {
		return request_code;
	}

	public void setRequest_code(String request_code) {
		this.request_code = request_code;
	}

}
