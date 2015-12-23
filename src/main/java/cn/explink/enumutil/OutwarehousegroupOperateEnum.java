package cn.explink.enumutil;

public enum OutwarehousegroupOperateEnum {

	TiHuo(1, "提货"), RuKu(2, "入库"), ChuKu(3, "出库"), FenZhanDaoHuo(4, "分站到货"), FenZhanLingHuo(5, "分站领货"), ZhongZhuanChuKu(6, "中转出站"), TuiHuoChuZhan(7, "退货出站"), ZhongZhuanZhanRuKu(8, "中转站入库"), ZhongZhuanZhanChuKu(
			9, "中转站出库"), TuiHuoZhanRuku(10, "退货站入库"), TuiHuoZhanZaiTou(11, "退货站再投"), TuiGongYingShangChuKu(12, "退供货商出库"), ErJiFenBo(13, "二级分拨"), KuDuiKuTuiHuoChuKu(14, "库对库退货出库"), EeJiZhanTuiHuoChuZhan(
			15, "二级站退货出站"), KuDuiKuChuKu(16, "库对库出库"), FenJianZhongZhuangChuKu(17, "分拣中转出库");

	private int value;
	private String text;

	private OutwarehousegroupOperateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}
