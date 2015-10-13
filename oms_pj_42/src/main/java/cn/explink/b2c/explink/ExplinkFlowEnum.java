package cn.explink.b2c.explink;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 允许被查询到的状态
 * 
 * @author Administrator
 *
 */
public enum ExplinkFlowEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈"), ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库扫描"), FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货"), FenZhanLingHuo(
			FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "派送员投递中"), YiFanKui(FlowOrderTypeEnum.YiFanKui.getValue(), "已反馈");

	private int flowordertype;
	private String text;

	private ExplinkFlowEnum(int flowordertype, String text) {
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
