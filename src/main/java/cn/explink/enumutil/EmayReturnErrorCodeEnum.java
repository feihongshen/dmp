package cn.explink.enumutil;

public enum EmayReturnErrorCodeEnum {
	WeiDaoHuo(1, "未到货"), TiHuo(2, "提货"), TiHuoYouHuoWuDan(3, "提货有货无单"), RuKu(4, "入库"), ChuKuSaoMiao(6, "出库扫描"), FenZhanDaoHuoSaoMiao(7, "分站到货扫描"), FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货"), FenZhanLingHuo(
			9, "分站领货"), TuiHuoZhanRuKu(15, "退货站入库"), TuiHuoZhanYouHuoWuDanRuKu(16, "退货站有货无单入库"), TuiHuoZhanZaiTouSaoMiao(17, "退货站再投扫描"),

	TuiGongYingShangChuKu(27, "退供货商出库"), GongYingShangJuShouFanKu(28, "供货商拒收返库"), CheXiaoFanKui(29, "撤销反馈"),
	// ChongZhengJiaoYi(30,"冲正交易"),
	ErJiFenBo(31, "二级分拨扫描"), KuDuiKuTuiHuoChuKu(32, "库对库退货出库"), EeJiZhanTuiHuoChuZhan(33, "二级站退货出站"), GongHuoShangTuiHuoChenggong(34, "供货商退货成功"), YiFanKui(35, "已反馈"), YiShenHe(36, "已审核"), DaoCuoHuoChuLi(
			38, "到错货处理"), TuiHuoChuZhan(40, "退货出站"), YiZhiFu(42, "POS支付"), YiChangDingDanChuLi(43, "异常订单处理"), DingDanLanJie(44, "订单拦截"), ShenHeWeiZaiTou(45, "审核为退货再投"), KuDuiKuChuKuSaoMiao(46,
			"库对库出库"), LanShouDaoHuo(53, "揽收到货");
	private int value;
	private String text;

	private EmayReturnErrorCodeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static EmayReturnErrorCodeEnum getText(int value) {
		for (EmayReturnErrorCodeEnum typeEnum : EmayReturnErrorCodeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}

	public static EmayReturnErrorCodeEnum getText(long value) {
		return getText((int) value);
	}
}
