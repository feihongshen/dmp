package cn.explink.b2c.sfexpress;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum SfCrossEnum {

	FenZhanDaohuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "50", "分站到货扫描-顺丰入库"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "44", "分站领货-派送员派送中"), TuoTou(
			FlowOrderTypeEnum.YiFanKui.getValue(), "80", "妥投-已反馈"), ZhiLiu(FlowOrderTypeEnum.YiFanKui.getValue(), "70", "滞留和拒收-已反馈"), // 统一改为滞留,顺丰70状态包括滞留和拒收
																																		// ,包含拒收关键词则认为是拒收，之后的状态不管。
	JuShou(FlowOrderTypeEnum.YiFanKui.getValue(), "33", "拒收-已反馈"), ShenHe(FlowOrderTypeEnum.YiShenHe.getValue(), "8000", "回单确认-审核") // 8000在顺丰属于回单确认状态，根据remark来判断是签收还是退回,签收人是
																																	// :退回"

	;

	private int flowordertype;
	private String sfcode; // 顺丰的状态码
	private String text;

	public String getSfcode() {
		return sfcode;
	}

	public void setSfcode(String sfcode) {
		this.sfcode = sfcode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	private SfCrossEnum(int flowordertype, String sfcode, String text) {
		this.flowordertype = flowordertype;
		this.sfcode = sfcode;
		this.text = text;

	}

}
