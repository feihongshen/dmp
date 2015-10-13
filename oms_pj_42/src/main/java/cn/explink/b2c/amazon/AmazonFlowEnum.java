package cn.explink.b2c.amazon;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

public enum AmazonFlowEnum {

	BaoGuoweiDao("SD", FlowOrderTypeEnum.DaoRuShuJu.getValue(), 0, "包裹未到"), RuKu("AF", FlowOrderTypeEnum.RuKu.getValue(), 0, "中转在途(入库)"), FenzhanDaohuoSaomiao("X4",
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), 0, "到站"), FenPeiXiaoJianYuan("OD", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), 0, "配送员出站"), TuoTou("D1", FlowOrderTypeEnum.YiShenHe
			.getValue(), DeliveryStateEnum.PeiSongChengGong.getValue(), "配送成功"), Jushou("RJ", FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.QuanBuTuiHuo.getValue(), "全部拒收"), Diushi("CA",
			FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.HuoWuDiuShi.getValue(), "包裹丢失"), Fenzhanzhiliu1("CA", FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.FenZhanZhiLiu
			.getValue(), "无法投递"), Fenzhanzhiliu2("SD", FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), "配送延迟"),

	ShanmenTuiDaoru("CR", FlowOrderTypeEnum.RuKu.getValue(), 0, "退货单导入"), ShanmenTuiLinghuo("CR", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), 0, "上门退领货"), ShanmenTuichenggong("CR",
			FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.ShangMenTuiChengGong.getValue(), "取货成功"), ShanmenHuanchenggong("CR", FlowOrderTypeEnum.YiShenHe.getValue(),
			DeliveryStateEnum.ShangMenHuanChengGong.getValue(), "取货成功"), ShanmenTuizhiliu("CR", FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), "上门退配送延迟"), Quhuoshibai(
			"CR", FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.ShangMenJuTui.getValue(), "取消取货"),

	TuiHuozhanRuku("AR", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), 0, "包裹回站"), TuiGongYingShangChuKu("DR", FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), 0, "退货离开分拨点");

	// AF
	// X4
	// OD
	// D1
	// RJ
	// CA
	// SD
	// AR
	// DR
	// CR

	private String amazon_code;
	private int onwer_code;
	private int delivery_state;
	private String desribe;

	private AmazonFlowEnum(String amazon_code, int onwer_code, int delivery_state, String desribe) {
		this.amazon_code = amazon_code;
		this.onwer_code = onwer_code;
		this.delivery_state = delivery_state;
		this.desribe = desribe;
	}

	public String getAmazon_code() {
		return amazon_code;
	}

	public void setAmazon_code(String amazon_code) {
		this.amazon_code = amazon_code;
	}

	public int getDelivery_state() {
		return delivery_state;
	}

	public void setDelivery_state(int delivery_state) {
		this.delivery_state = delivery_state;
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
