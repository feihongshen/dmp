package cn.explink.b2c.saohuobang;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 扫货帮推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum SaohuobangFlowEnum {
	jiedan("ACCEPT", FlowOrderTypeEnum.DaoRuShuJu.getValue(), "接单"),
	// ChuKuSaoMiao(126,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库分拣中心"),
	ruku("GOT", FlowOrderTypeEnum.RuKu.getValue(), "入库到分站"),
	// FenZhanLingHuo(140,FlowOrderTypeEnum.FenZhanLingHuo.getValue(),"分站领货派送"),
	PeiSongChengGong("SIGNED", FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功"),
	// 暂时没有这样的业务
	ShangMenTuiChengGong("SIGNED", FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功"), ShangMenHuanChengGong("SIGNED", FlowOrderTypeEnum.YiShenHe.getValue(), "上门换成功"),
	// BuFenShiBai(175,FlowOrderTypeEnum.YiShenHe.getValue(),"部分失败"),
	// FenZhanZhiLiu("",FlowOrderTypeEnum.YiShenHe.getValue(),"分站滞留"),
	JuShou("FAILED", FlowOrderTypeEnum.YiShenHe.getValue(), "配送失败"), TuiHuoZhanRuKu("FAILED", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "退货站入库"), TuiGongYingShangChuKu("FAILED",
			FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "退供货商出库");

	public String getState() {
		return state;
	}

	public void setState(String state) {
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

	private String state;
	private int flowordertype;
	private String text;

	private SaohuobangFlowEnum(String state, int flowordertype, String text) {
		this.state = state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

}
