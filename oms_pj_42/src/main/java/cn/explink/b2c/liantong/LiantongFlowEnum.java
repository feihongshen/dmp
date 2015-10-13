package cn.explink.b2c.liantong;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 杭州ABC推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum LiantongFlowEnum {
	// 配送中
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "库房入库"), Chuku(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货"), FenZhanLingHuo(
			FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货"), YiShenHe(FlowOrderTypeEnum.YiShenHe.getValue(), "派送结果")

	;

	private int flowordertype;

	private String text;

	private LiantongFlowEnum(int flowordertype, String text) {
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
