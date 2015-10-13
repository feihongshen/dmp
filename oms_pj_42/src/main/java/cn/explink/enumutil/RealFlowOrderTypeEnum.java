package cn.explink.enumutil;

public enum RealFlowOrderTypeEnum {
	DaoRuShuJu(1, "导入数据"), TiHuo(2, "提货"), TiHuoYouHuoWuDan(3, "提货有货无单"), KuFangRuKu(4, "库房入库"), ZhongZhuanZhanRuKu(5, "中转站入库"), KuFangChuKuSaoMiao(6, "库房出库"), ZhongZhuanZhanChuZhanSaoMiao(7, "中转出站"), ZhongZhuanZhanChuKuSaoMiao(
			8, "中转站出库"), TuiHuoZhanChuKuSaoMiao(9, "退货站出库"), FenZhanDaoHuoSaoMiao(10, "分站到货"), KuFangYouHuoWuDanSaoMiao(11, "库房到错货"), FenZhanDaoHuoYouHuoWuDanSaoMiao(12, "分站到错货"), ZhongZhuanZhanYouHuoWuDanSaoMiao(
			13, "中转站到错货"), TuiHuoZhanYouHuoWuDanSaoMiao(14, "退货站到错货"), FenZhanLingHuo(15, "分站领货"), TuiHuoZhanRuKu(16, "退货站入库"), TuiGongYingShangChuKu(17, "退供货商出库"), GongYingShangJuShouFanKu(18,
			"供货商拒收返库"), CheXiaoFanKui(19, "撤销反馈"), GongHuoShangTuiHuoChenggong(20, "供货商退货成功"), YiFanKui(21, "已反馈"), YiShenHe(22, "已审核"), UpdateDeliveryBranch(23, "更新配送站"), DaoCuoHuoChuLi(24, "到错货处理"), BeiZhu(
			25, "备注"), TuiHuoChuZhan(26, "退货出站"), ShouGongXiuGai(27, "手工修改"), PosZhiFu(28, "POS支付"), YiChangDingDanChuLi(29, "异常订单处理"), DingDanLanJie(30, "订单拦截"), ShenHeWeiZaiTou(31, "审核为退货再投"), ;

	private int value;
	private String text;

	private RealFlowOrderTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static RealFlowOrderTypeEnum getText(int value) {
		for (RealFlowOrderTypeEnum typeEnum : RealFlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}

	public static RealFlowOrderTypeEnum getText(long value) {
		for (RealFlowOrderTypeEnum typeEnum : RealFlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
