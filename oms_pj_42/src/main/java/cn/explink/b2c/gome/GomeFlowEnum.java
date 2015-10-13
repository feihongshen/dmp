package cn.explink.b2c.gome;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum GomeFlowEnum {

	RuKu("3AP", FlowOrderTypeEnum.RuKu.getValue(), "3PL已揽收"), FenzhanDaohuoSaomiao("LH", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "送达目的地分拨点"), FenPeiXiaoJianYuan("DO",
			FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "订单已出分拨点"), TuoTou("DL", FlowOrderTypeEnum.YiShenHe.getValue(), "订单已妥投"), FenZhanZhiLiu("DF", FlowOrderTypeEnum.YiShenHe.getValue(), "订单未送达"), JuShou(
			"RV", FlowOrderTypeEnum.YiShenHe.getValue(), "订单全部拒收"), Diushi("MS", FlowOrderTypeEnum.YiShenHe.getValue(), "丢件"),

	ShangmenTuilinghuo("RDO", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "退货订单取货途中"), ShangMentuiChengGong("RSA", FlowOrderTypeEnum.YiShenHe.getValue(), "退货送达分拨点"), TuiHuoChuZhan("RSD",
			FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue(), "退货离开分拨点"),

	Tuihuozhanruku("RSA", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "退货送达分拨点"), Tuigongyingshangchuku("RSD", FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "退货离开分拨点"),

	Yichangchuli("MS", FlowOrderTypeEnum.YiChangDingDanChuLi.getValue(), "手工维护破损"),

	ShangMentuiJutui("RCF", FlowOrderTypeEnum.YiShenHe.getValue(), "退货订单失败");

	// 3AP 3PL 已揽收
	// LH 送达目的地分拨点
	// AC 配送派工完成
	// DO 订单已出分拨点
	// DL 订单已妥投
	// DF 订单未送达
	// RV 订单全部拒收
	// MS 丢件

	// RDO 退货订单取货途中
	// RSA 退货送达分拨点
	// RSD 退货离开分拨点
	// RCF 退货订单失败

	private String gome_code;
	private int onwer_code;
	private String desribe;

	private GomeFlowEnum(String gome_code, int onwer_code, String desribe) {
		this.gome_code = gome_code;
		this.onwer_code = onwer_code;
		this.desribe = desribe;
	}

	public String getGome_code() {
		return gome_code;
	}

	public void setGome_code(String gome_code) {
		this.gome_code = gome_code;
	}

	public int getOnwer_code() {
		return onwer_code;
	}

	public void setOnwer_code(int onwer_code) {
		this.onwer_code = onwer_code;
	}

	public String getDesribe() {
		return desribe;
	}

	public void setDesribe(String desribe) {
		this.desribe = desribe;
	}

}
