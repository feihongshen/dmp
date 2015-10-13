package cn.explink.b2c.happygo;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum HappyGoEnum {
	RuKu(4, "入库", "IntoWarehous"), ChuKuSaoMiao(6, "出库扫描", "OutWarehouse"), FenZhanDaoHuoSaoMiao(7, "分站到货扫描", "SubstationGoods"), FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "分站放错货扫描", "SubstationGoodsNoList"), FenZhanLingHuo(
			9, "分站领货", "ReceiveGoods"), ZhongZhuanChuKuSaoMiao(10, "中转出站扫描", "ChangeGoodsOutwarehouse"), TuiHuoChuKuSaoMiao(40, "退货出站扫描", "ReturnGoodsOutwarehouse"), ZhongZhuanZhanRuKu(12, "中转站入库",
			"ChangeIntoWarehous"), ZhongZhuanZhanChuKuSaoMiao(14, "中转站出库扫描", "TransBranchOutWarehouse"), TuiHuoZhanRuKu(15, "退货站入库", "BackIntoWarehous"), PeiSongChengGong(18, "配送成功", "Success"), ShangMenTuiChengGong(
			19, "上门退成功", "BackToDoorSuccess"), ShangMenHuanChengGong(20, "上门换成功", "ChangeToDoorSuccess"), JuShou(21, "拒收", "ReturnGoods"), BuFenTuiHuo(22, "部分拒收", "SomeReturnGoods"), FenZhanZhiLiu(
			23, "分站滞留", "StayGoods"), ShangMenJuTui(24, "上门拒退", "BackToDoorFail"), YiShenHe(36, "已审核", "yishenhe");

	private int value;
	private String text;
	private String method;

	private HappyGoEnum(int value, String text, String method) {
		this.value = value;
		this.text = text;
		this.method = method;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public String getMethod() {
		return method;
	}

	public static FlowOrderTypeEnum getText(int value) {
		for (FlowOrderTypeEnum typeEnum : FlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}

}
