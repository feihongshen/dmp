package cn.explink.b2c.dangdang;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * tmall推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum DangDangFlowEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货"), DaoCuoHuo(
			FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "分站到错货"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送"), YiShenHe(FlowOrderTypeEnum.YiShenHe
			.getValue(), "已审核");

	private int flowordertype;
	private String text;

	private DangDangFlowEnum(int flowordertype, String text) {
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
