package cn.explink.b2c.dongfangcj;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 允许被查询到的状态
 * 
 * @author Administrator
 *
 */
public enum DongFangCJFlowEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "商品入仓"), ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "商品出仓"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "带出配送"), YiShenHe(
			FlowOrderTypeEnum.YiShenHe.getValue(), "配送结果反馈");

	private int flowordertype;
	private String text;

	private DongFangCJFlowEnum(int flowordertype, String text) {
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
