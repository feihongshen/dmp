package cn.explink.b2c.lefeng;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum LefengFlowEnum {

	// 入库 出库到货，领货 N10
	// 成功 S00
	// 拒收 E30
	// 滞留E31 E32
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "N10", "订单入库"),
	ChukuSaomiao(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"N10","库房出库扫描"),
	FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),"N10","分站到货扫描"),
	Deliverying(FlowOrderTypeEnum.FenZhanLingHuo.getValue(),"N10","订单派送中"),
	Received(FlowOrderTypeEnum.YiShenHe.getValue(),"S00","配送成功签收"),
	DeliveryFailed(FlowOrderTypeEnum.YiShenHe.getValue(),"E30","配送失败-客户拒收"),
	DeliveryException(FlowOrderTypeEnum.YiShenHe.getValue(),"E31","配送异常-无人签收"),
	DeliveryAddressError(FlowOrderTypeEnum.YiShenHe.getValue(),"E32","配送异常-地址错误,未能送达");
	private long flowtype;
	private String cmcode;
	private String text;
	public long getFlowtype() {
		return flowtype;
	}
	public void setFlowtype(long flowtype) {
		this.flowtype = flowtype;
	}
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
	private LefengFlowEnum(long flowtype, String cmcode, String text) {
		this.flowtype = flowtype;
		this.cmcode = cmcode;
		this.text = text;
	}
	


}
