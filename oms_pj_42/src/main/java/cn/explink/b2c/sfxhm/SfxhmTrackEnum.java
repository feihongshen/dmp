package cn.explink.b2c.sfxhm;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */
public enum SfxhmTrackEnum {
	DaoRuShuJu(FlowOrderTypeEnum.DaoRuShuJu.getValue(), "30", "导入数据-顺丰装车发货"), Ruku(FlowOrderTypeEnum.RuKu.getValue(), "31", "入库-卸车到分拣中心"), Chuku(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "30",
			"出库-分拣至站点为装车"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "31", "分站到货-站点卸车"), PaiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "44", "派送中-派件出仓"), QianShou(
			FlowOrderTypeEnum.YiShenHe.getValue(), "80", "签收"), ZhiLiu(FlowOrderTypeEnum.YiShenHe.getValue(), "70", "滞留"),

	;

	private long flowordertype;
	private String state;
	private String text;

	private SfxhmTrackEnum(long flowordertype, String state, String text) {
		this.state = state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

}
