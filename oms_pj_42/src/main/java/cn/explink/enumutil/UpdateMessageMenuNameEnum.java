package cn.explink.enumutil;

public enum UpdateMessageMenuNameEnum {
	KuFangChuKuTongJi(1, "DeliveryChuku", "库房出库统计(云)"), TuoTOuDingDanHuiZong(2, "DeliverySuccessful", "妥投订单汇总(云)"), FenZhanDaoHuoTongJi(3, "DeliveryDaohuo", "分站到货统计(云)"),
	// ZhanDianDaoHuoHuiZong("DeliveryDaohuo","站点到货汇总(云)"),
	ZhiLiuDingDanHuiZong(4, "DeliveryZhiLiu", "滞留订单汇总(云)"), JuShouDingDanHuiZong(5, "DeliveryJuShou", "拒收订单汇总(云)"), TuiHuoChuZhanTongJi(6, "TuiHuoChuZhanOrder", "退货出站统计(云)"), ZhongZhuanDingDanTongJi(
			7, "ZhongZhuan", "中转订单统计(云)"), TiHuoDingDanTongJi(8, "CwbTiHuo", "提货订单统计(云)"), KDKKuFangChuKuTongJi(9, "KDKDeliveryChuku", "库对库出库统计(云)"), TuiHuoZhanRuKuTongJi(10, "TuiHuoZhanRuKuTongJi",
			"退货站入库统计(云)"), KuFangRuKuTongJi(11, "KuFangRuKuTongJi", "库房入库统计(云)"), KuFangZaiTuTongJi(12, "KuFangZaiTuTongJi", "库房在途统计(云)"), ZongHeChaXun(13, "ZongHeChaXun", "综合查询(云)");

	private int value;
	private String calassname;
	private String text;

	private UpdateMessageMenuNameEnum(int value, String calassname, String text) {
		this.value = value;
		this.calassname = calassname;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getCalassname() {
		return calassname;
	}

	public String getText() {
		return text;
	}

}
