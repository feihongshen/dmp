package cn.explink.b2c.vipshop;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * vipshop推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum VipShopFlowEnum {
	Ruku(4, FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), ChuKuSaoMiao(4, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库扫描"), FenZhanDaoHuo(4, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
			"分站到货扫描"), FenZhanLingHuo(4, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"),

	PeiSongChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"), ShangMenTuiChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功"), ShangMenHuanChengGong(5, FlowOrderTypeEnum.YiShenHe
			.getValue(), "上门换成功"),

	JuShou(-2, FlowOrderTypeEnum.YiShenHe.getValue(), "拒收"), FenZhanZhiLiu(4, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), HuoWuDiuShi(300, FlowOrderTypeEnum.YiShenHe.getValue(), "货物丢失");

	public int getVipshop_state() {
		return vipshop_state;
	}

	public void setVipshop_state(int vipshop_state) {
		this.vipshop_state = vipshop_state;
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

	private int vipshop_state;
	private int flowordertype;
	private String text;

	private VipShopFlowEnum(int vipshop_state, int flowordertype, String text) {
		this.vipshop_state = vipshop_state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

}
