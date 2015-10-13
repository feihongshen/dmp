package cn.explink.b2c.chinamobile;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum ChinamobileFlowEnum {

	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "EBIZQHWC", "库房入库-取货完成"),
	// ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"EBIZPSZT","库房出库-配送在途"),
	fenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "EBIZPSZT", "分站到货扫描-配送在途"), FenZhanLingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "EBIZPSZT", "分站领货-配送在途"), QianShou(
			FlowOrderTypeEnum.YiShenHe.getValue(), "EBIZPSCG", "配送成功"), // 配送成功，上门换成功
																		// ,上门退成功
	YiChangQianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "EBIZPSYC", "配送失败（拒收）"), FenZhanZhiLiu(FlowOrderTypeEnum.YiShenHe.getValue(), "EBIZPSZT", "分站滞留-配送在途"), ;

	private long flowtype;
	private String cmcode;

	public String getCmcode() {
		return cmcode;
	}

	public void setCmcode(String cmcode) {
		this.cmcode = cmcode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setLetvcode(String letvcode) {
		this.cmcode = letvcode;
	}

	public long getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(long flowtype) {
		this.flowtype = flowtype;
	}

	private String text;

	private ChinamobileFlowEnum(long flowtype, String cmcode, String text) {
		this.flowtype = flowtype;
		this.cmcode = cmcode;
		this.text = text;
	}

}
