package cn.explink.enumutil;

public enum ModelEnum {// 加jsp文件名
	TuoTouDingdanhuizong(1, "妥投订单汇总", "tuotoushow"), JuShouDingdanhuizong(2, "拒收订单汇总", "jushoushow"), ZhiLiuDingdanhuizong(3, "滞留订单汇总", "zhiliushow"), KuFangChuKuDingdantongji(4, "库房出库订单统计",
			"kufangchukushow"), FenZhanDaoHuotongji(5, "分站到货统计", "daohuoshow"), ZhanDianDaoHuohuizong(6, "站点到货汇总", "daohuohuizongshow"), TuiHuoChuZhanTongJi(7, "退货出站统计", "tuihuochuzhantongji"), ZhongZhuanDingDanTongJi(
			8, "中转订单统计", "zhongzhuandingdanshow"), TiHuoDingDanTongJi(9, "提货订单统计", "tihuodingdan"), KuFangChuKuHuiZong(10, "库房出库汇总", "kufangchukuhuizong"), KDKChuKuDingdantongji(11, "库对库出库统计",
			"kuduikuchukutongji"), TuiHuoZhanRuKuTongJi(12, "退货站入库统计", "tuihuozhanrukutongji"), KuFangRuKuTongJi(13, "库房入库统计", "kufangrukutongji"), KuFangZaiTuTongJi(14, "库房在途统计", "kufangzaitutongji"), KeHuFaHuoTongJi(
			15, "客户发货统计", "kehufahuotongji"), KeHuFaHuoHuiZong(16, "客户发货汇总", "kehufahuohuizong"), ZongHeChaXun(17, "综合查询", "zonghelist"), FenZhanDaoHuoHuiZong(18, "分站到货汇总", "fenzhandaohuohuizong"), DanliangChaxun(
			19, "单量查询", "danliangchaxun"), deliveryRate(20, "妥投率查询", "");

	private int value;
	private String text;
	private String jspName;

	private ModelEnum(int value, String text, String jspName) {
		this.value = value;
		this.text = text;
		this.jspName = jspName;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public String getJspName() {
		return jspName;
	}

	public static ModelEnum getByValue(int value) {
		for (ModelEnum modelEnum : ModelEnum.values()) {
			if (value == modelEnum.getValue()) {
				return modelEnum;
			}
		}
		return null;
	}
}
