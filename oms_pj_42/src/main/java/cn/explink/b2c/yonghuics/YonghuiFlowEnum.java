package cn.explink.b2c.yonghuics;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 一号店推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum YonghuiFlowEnum {
	Ruku(1, FlowOrderTypeEnum.RuKu.getValue(), "入库到分拣中心"), ChuKuSaoMiao(1, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库分拣中心"), FenZhanDaoHuo(1, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
			"入库到分站"), FenZhanLingHuo(3, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"), PeiSongChengGong(2, FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"), JuShou(9,
			FlowOrderTypeEnum.YiShenHe.getValue(), "拒收"), ZhiLiu(10, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留-未妥投"),

	;

	private int state;
	private int flowordertype;
	private String text;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	private YonghuiFlowEnum(int state, int flowordertype, String text) {
		this.state = state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

}
