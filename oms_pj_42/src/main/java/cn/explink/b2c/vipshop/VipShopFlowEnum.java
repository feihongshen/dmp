package cn.explink.b2c.vipshop;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * vipshop推送状态反馈的枚举
 * 
 * @author Administrator
 * 
 */
public enum VipShopFlowEnum {
	Ruku(411, FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), 
	ChuKuSaoMiao(412, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库扫描"), 
	FenZhanDaoHuo(413, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),"分站到货扫描"), 
	FenZhanLingHuo(33, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货即将派送"), // 由于唯品会需求变动，把此功能转化为, 即将配送,33
	
	FenZhanLingHuo_temp(4, -10, "分派派送员"), // 由于唯品会需求变动，把此功能转化为, 即将配送,33
	
	PeiSongChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"), 
	ShangMenTuiChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功"), 
	ShangMenHuanChengGong(5, FlowOrderTypeEnum.YiShenHe.getValue(), "上门换成功"),
	JuShou(-2, FlowOrderTypeEnum.YiShenHe.getValue(), "拒收"), 
	BuFenJuShou(-2, FlowOrderTypeEnum.YiShenHe.getValue(), "部分拒收"), 
	FenZhanZhiLiu(4, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), 
	HuoWuDiuShi(-1, FlowOrderTypeEnum.YiShenHe.getValue(), "货物丢失"),// 上门退
	Ruku_t(31, FlowOrderTypeEnum.DaoRuShuJu.getValue(), "承运商接单"), 
	FenZhanLingHuo_t(32, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "指定快递员"), 
	ShangMenLanJian_t(34, FlowOrderTypeEnum.YiShenHe.getValue(), "上门揽件"), // 和审核一起
	ShangMenTuiChengGong_t(35, FlowOrderTypeEnum.YiShenHe.getValue(), "揽件成功-揽件入站"), // 揽件成功
	ShengMenJuTui_t(36, FlowOrderTypeEnum.YiShenHe.getValue(), "揽件失败"), ;

	public int getVipshop_state() {
		return this.vipshop_state;
	}

	public void setVipshop_state(int vipshop_state) {
		this.vipshop_state = vipshop_state;
	}

	public int getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getText() {
		return this.text;
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
