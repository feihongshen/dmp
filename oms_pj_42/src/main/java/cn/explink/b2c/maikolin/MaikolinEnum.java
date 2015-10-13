package cn.explink.b2c.maikolin;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum MaikolinEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"), YiShenHe(
			FlowOrderTypeEnum.YiShenHe.getValue(), "已审核");

	private int flowordertype;
	private String text;

	private MaikolinEnum(int flowordertype, String text) {
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
