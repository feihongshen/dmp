package cn.explink.enumutil;

public enum ChangeFunctionidEnum {
	TuoTouDingDanHuiZong(1, "妥投订单汇总（云）"), ZhiLiuDingDanHuiZong(2, "滞留订单汇总（云）"), KuFangChuKuTongJi(3, "库房出库统计（云）"), FenZhanDaoHuoTongJi(4, "分站到货统计（云）/站点到货汇总（云）"), ZhongZhuanDingDanTongJi(5,
			"中转订单统计（云）"), JuShouDingDanHuiZong(6, "拒收订单汇总（云）"), TiHuoDingDanTongJi(7, "提货订单统计（云）"), TuiHuoChuZhanTongJi(8, "退货出站统计(云)"), KDKTuiHuoChuZhanTongJi(9, "库对库出库统计(云)"), KuFangRuKuTongJi(10,
			"库房入库统计(云)"), KuFangZaiTuTongJi(11, "库房在途统计(云)"), TuiHuoZhanRuKuTongJi(12, "退货站入库统计(云)");

	private int value;
	private String text;

	private ChangeFunctionidEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ChangeFunctionidEnum getText(int value) {
		for (ChangeFunctionidEnum typeEnum : ChangeFunctionidEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
