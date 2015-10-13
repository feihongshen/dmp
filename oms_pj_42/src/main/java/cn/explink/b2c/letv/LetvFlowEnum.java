package cn.explink.b2c.letv;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum LetvFlowEnum {

	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "100", "库房入库-出库揽收"), ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "101", "库房出库-运单发运"), fenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
			"200", "分站到货扫描-派件揽收"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "201", "分站领货-派件发运"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "300", "签收"), // 配送成功，上门换成功，不包含上门退成功
	YiChangQianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "400", "拒收和滞留-异常签收"), ShangMenTuiChengGong(FlowOrderTypeEnum.YiShenHe.getValue(), "500", "上门退成功-退货取件揽收"),

	;
	private long flowtype;
	private String letvcode;

	public String getLetvcode() {
		return letvcode;
	}

	public void setLetvcode(String letvcode) {
		this.letvcode = letvcode;
	}

	public long getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(long flowtype) {
		this.flowtype = flowtype;
	}

	private String text;

	private LetvFlowEnum(long flowtype, String letvcode, String text) {
		this.flowtype = flowtype;
		this.letvcode = letvcode;
		this.text = text;
	}

	public String getSmile_code() {
		return letvcode;
	}

	public void setSmile_code(String smile_code) {
		this.letvcode = smile_code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
