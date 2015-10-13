package cn.explink.b2c.tmall;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * tmall推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum TmallFlowEnum {
	TMS_ACCEPT("TMS_ACCEPT", FlowOrderTypeEnum.DaoRuShuJu.getValue(), "入库信息反馈"), // 同时推送库房进
																					// TMS_STATION_IN
	TMS_STATION_OUT("TMS_STATION_OUT", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库"), TMS_STATION_IN("TMS_STATION_IN", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站进"), TMS_DELIVERING(
			"TMS_DELIVERING", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站派送"), // 同时推送分站出
																					// TMS_STATION_OUT

	/**
	 * 以下部分注意一下。===================================================
	 */
	TMS_ERROR("TMS_ERROR", FlowOrderTypeEnum.YiShenHe.getValue(), "滞留丢失异常"), TMS_SIGN("TMS_SIGN", FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功,包括支付成功"), TMS_FAILED("TMS_FAILED",
			FlowOrderTypeEnum.YiShenHe.getValue(), "拒收");

	private String tmall_state;
	private int flowordertype;
	private String text;

	private TmallFlowEnum(String tmall_state, int flowordertype, String text) {
		this.tmall_state = tmall_state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

	public String getTmall_state() {
		return tmall_state;
	}

	public void setTmall_state(String tmall_state) {
		this.tmall_state = tmall_state;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
