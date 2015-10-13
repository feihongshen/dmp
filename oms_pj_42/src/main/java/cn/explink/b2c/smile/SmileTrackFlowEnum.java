package cn.explink.b2c.smile;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 思迈速递 跟踪日志反馈允许反馈的状态
 * 
 * @author Administrator
 *
 */
public enum SmileTrackFlowEnum {
	RuKu(FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), ChuKuSaoMiao(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库"), FenZhanRuKu(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货扫描"), FenZhanLingHuo(
			FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"),

	PeiSongChengGong(FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"), ShangMenTuiChengGong(FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功"), ShangMenHuanChengGong(FlowOrderTypeEnum.YiShenHe.getValue(),
			"上门换成功"),

	JuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "拒收"), FenZhanZhiLiu(FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), HuoWuDiuShi(FlowOrderTypeEnum.YiShenHe.getValue(), "货物丢失")

	;

	private int flowordertype;
	private String text;

	private SmileTrackFlowEnum(int flowordertype, String text) {

		this.flowordertype = flowordertype;
		this.text = text;
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
