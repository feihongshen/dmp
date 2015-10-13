package cn.explink.b2c.homegobj;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */
public enum HomegobjTrackEnum {
	DaoRuShuJu(FlowOrderTypeEnum.DaoRuShuJu.getValue(), "10", "导入数据"), Ruku(FlowOrderTypeEnum.RuKu.getValue(), "20", "入库"), Chuku(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "25", "出库"), FenZhanDaoHuo(
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "25", "分站到货"), PaiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "25", "派送中"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(),
			"30", "签收"), PaySuccess(FlowOrderTypeEnum.YiShenHe.getValue(), "31", "支付成功"),

	JuShou1(FlowOrderTypeEnum.YiShenHe.getValue(), "40", "现场拒收"), JuShou2(FlowOrderTypeEnum.YiShenHe.getValue(), "41", "非现场拒收"), QiuShi(FlowOrderTypeEnum.YiShenHe.getValue(), "50", "托寄物品丢失"),

	YiChang1(FlowOrderTypeEnum.YiShenHe.getValue(), "60", "异常-联系不上客户"), YiChang2(FlowOrderTypeEnum.YiShenHe.getValue(), "61", "异常-客户推迟收货"),

	;

	private long flowordertype;
	private String state;
	private String text;

	private HomegobjTrackEnum(long flowordertype, String state, String text) {
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
