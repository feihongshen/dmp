package cn.explink.enumutil;

public enum FlowOrderTypeEnum {
	DaoRuShuJu(1, "导入数据", "ImportCwb"),
	// YouHuoWuDanDaoRuShuJu(2,"有货无单导入数据","NoListImportCwb"),
	TiHuo(2, "提货", "GetGoods"), TiHuoYouHuoWuDan(3, "提货有货无单", "GetGoodsnoListIntoWarehous"), RuKu(4, "入库", "IntoWarehous"), YouHuoWuDan(5, "有货无单入库", "NoListIntoWarehous"), ChuKuSaoMiao(6, "出库",
			"OutWarehouse"), FenZhanDaoHuoSaoMiao(7, "分站到货扫描", "SubstationGoods"), FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货", "SubstationGoodsNoList"), FenZhanLingHuo(9, "领货", "ReceiveGoods"), ZhongZhuanChuKuSaoMiao(
			10, "中转出站扫描", "ChangeGoodsOutwarehouse"), TuiHuoChuKuSaoMiao(40, "退货出站扫描", "ReturnGoodsOutwarehouse"), ZhongZhuanZhanRuKu(12, "中转站入库", "ChangeIntoWarehous"), ZhongZhuanZhanYouHuoWuDanRuKu(
			13, "中转站有货无单入库", "ChangeNoListIntoWarehous"), ZhongZhuanZhanChuKuSaoMiao(14, "中转站出库扫描", "TransBranchOutWarehouse"), TuiHuoZhanRuKu(15, "退货站入库", "BackIntoWarehous"), TuiHuoZhanYouHuoWuDanRuKu(
			16, "退货站有货无单入库", "BackNoListIntoWarehous"), TuiHuoZhanZaiTouSaoMiao(17, "退货站再投扫描", "BackReturnOutWarehous"), PeiSongChengGong(18, "配送成功", "Success"), ShangMenTuiChengGong(19, "上门退成功",
			"BackToDoorSuccess"), ShangMenHuanChengGong(20, "上门换成功", "ChangeToDoorSuccess"), JuShou(21, "拒收", "ReturnGoods"), BuFenTuiHuo(22, "部分拒收", "SomeReturnGoods"), FenZhanZhiLiu(23, "分站滞留",
			"StayGoods"), ShangMenJuTui(24, "上门拒退", "BackToDoorFail"), HuoWuDiuShi(25, "货物丢失", "GoodsLose"), TuiGongYingShangChuKu(27, "退供货商出库", "ReturnCustomer"), GongYingShangJuShouFanKu(28,
			"供货商拒收返库", "CustomRefuse"), CheXiaoFanKui(29, "撤销反馈", "DeliverStatePodCancel"), ChongZhengJiaoYi(30, "冲正交易", "toReverseDept"), ErJiFenBo(31, "二级分拨扫描", "Cwbresetbranch"), KuDuiKuTuiHuoChuKu(
			32, "库对库退货出库", "Warehouseback"), EeJiZhanTuiHuoChuZhan(33, "二级站退货出站", "Secondbranchback"), GongHuoShangTuiHuoChenggong(34, "供货商退货成功", "SupplierBackSuccess"), UpdateDeliveryBranch(37,
			"更新配送站", "UpdateDeliveryBranch"), DaoCuoHuoChuLi(38, "到错货处理", "DaoCuoHuoChuLi"), BeiZhu(39, "备注", "BeiZhu"), TuiHuoChuZhan(40, "退货出站", "TuiHuoChuZhan"), ShouGongXiuGai(41, "手工修改",
			"ShouGongXiuGai"), PosZhiFu(42, "POS支付", "poszhifu"), YiChangDingDanChuLi(43, "异常订单处理", "yichangchuli"), DingDanLanJie(44, "订单拦截", "DingDanLanJie"), ShenHeWeiZaiTou(45, "审核为退货再投",
			"ShenHeWeiZaiTou"), YiFanKui(35, "反馈", "yifankui"), YiShenHe(36, "审核", "yishenhe"), BaoGuoweiDao(50, "包裹未到", "包裹未到"), // 亚马逊对接使用
	ZhongZhuanyanwu(51, "中转延误", "中转延误"), // 亚马逊对接使用
	ShouGongdiushi(52, "货物丢失", "货物丢失"), ZiTiYanWu(53, "自提已超5天", "自提已超5天"), // 亚马逊对接使用
	KuDuiKuChuKuSaoMiao(46, "库对库出库", "库对库出库"),UpdatePickBranch(63,"更新提货站","更新提货站");
	private int value;
	private String text;
	private String method;

	private FlowOrderTypeEnum(int value, String text, String method) {
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
