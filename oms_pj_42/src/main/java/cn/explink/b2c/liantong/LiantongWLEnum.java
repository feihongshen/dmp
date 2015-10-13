package cn.explink.b2c.liantong;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum LiantongWLEnum {
	// 配送中
	DaoRuShuJu(FlowOrderTypeEnum.DaoRuShuJu.getValue(), "ORDE", "下单成功-导入excel"), Ruku(FlowOrderTypeEnum.RuKu.getValue(), "TAKE", "库房入库-揽收成功"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo
			.getValue(), "OWAY", "分站领货"), QianShouChengGong(FlowOrderTypeEnum.YiShenHe.getValue(), "SIGN", "签收成功"), QianShouShiBai(FlowOrderTypeEnum.YiShenHe.getValue(), "FAIL", "签收失败"),

	;

	private int flowordertype;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private String text;

	private LiantongWLEnum(int flowordertype, String code, String text) {
		this.flowordertype = flowordertype;
		this.code = code;
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