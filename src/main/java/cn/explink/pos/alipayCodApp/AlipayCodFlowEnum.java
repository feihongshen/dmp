package cn.explink.pos.alipayCodApp;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 允许被查询到的状态 支付宝移动app查询
 * 
 * @author Administrator
 *
 */
public enum AlipayCodFlowEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库扫描"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货"), FenZhanLingHuo(
			FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "派送员投递中"), YiFanKui(FlowOrderTypeEnum.YiFanKui.getValue(), "投递结果反馈");

	private int flowordertype;
	private String text;

	private AlipayCodFlowEnum(int flowordertype, String text) {
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
