package cn.explink.b2c.mmb;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum MmbFlowEnum {

	DaoRuShuJu(FlowOrderTypeEnum.DaoRuShuJu.getValue(), "1", "导入数据-已揽收"), Ruku(FlowOrderTypeEnum.RuKu.getValue(), "2", "库房入库-在途"), fenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
			"4", "分站到货-到达当地"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "5", "分站领货-投递中"), TuiGongHuoShangChengGong(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), "6",
			"退供货商成功-未妥投已退回"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "7", "审核成功-已签收"), // 配送成功，上门换成功，不包含上门退成功
	JuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "8", "拒收-未妥投开始退回"),

	;
	private long flowtype;
	private String mmbcode;
	private String text;

	public String getMmbcode() {
		return mmbcode;
	}

	public void setMmbcode(String mmbcode) {
		this.mmbcode = mmbcode;
	}

	public long getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(long flowtype) {
		this.flowtype = flowtype;
	}

	private MmbFlowEnum(long flowtype, String mmbcode, String text) {
		this.flowtype = flowtype;
		this.mmbcode = mmbcode;
		this.text = text;
	}

	public String getSmile_code() {
		return mmbcode;
	}

	public void setSmile_code(String smile_code) {
		this.mmbcode = smile_code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
