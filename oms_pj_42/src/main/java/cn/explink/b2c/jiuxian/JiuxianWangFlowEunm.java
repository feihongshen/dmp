package cn.explink.b2c.jiuxian;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum JiuxianWangFlowEunm {
	Ruku(124, FlowOrderTypeEnum.RuKu.getValue(), "入库到分拣中心"), ChuKuSaoMiao(126, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库分拣中心"), FenZhanDaoHuo(130, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao
			.getValue(), "入库到分站"), FenZhanLingHuo(140, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"), PeiSongChengGong(170, FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"),
	// 暂时没有这样的业务
	ShangMenTuiChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功"), ShangMenHuanChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "上门换成功"), BuFenShiBai(175, FlowOrderTypeEnum.YiShenHe
			.getValue(), "部分失败"), FenZhanZhiLiu(190, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), JuShou(180, FlowOrderTypeEnum.YiShenHe.getValue(), "配送失败"), TuiHuoZhanRuKu(234,
			FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "退货站入库"), TuiGongYingShangChuKu(236, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "退供货商出库");

	public int getYihaodian_state() {
		return yihaodian_state;
	}

	public void setYihaodian_state(int yihaodian_state) {
		this.yihaodian_state = yihaodian_state;
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

	private int yihaodian_state;
	private int flowordertype;
	private String text;

	private JiuxianWangFlowEunm(int yihaodian_state, int flowordertype, String text) {
		this.yihaodian_state = yihaodian_state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

}
