package cn.explink.enumutil;

public enum PrintTemplateOpertatetypeEnum {
	ChuKuAnDan(1, "出库交接单按单"), ChuKuHuiZong(2, "出库交接单汇总"), TuiHuoChuZhanAnDan(3, "退货出站交接单按单"), TuiHuoChuZhanHuiZong(4, "退货出站交接单汇总"), LingHuoAnDan(5, "领货交接单按单"), LingHuoHuiZong(6, "领货交接单汇总"), TuiGongYingShangChuKuAnDan(
			7, "退供货商出库交接单按单"), TuiGongYingShangChuKuHuiZong(8, "退供货商出库交接单汇总"), ZhongZhuanChuZhanAnDan(9, "中转出站交接单按单"), ZhongZhuanChuZhanHuiZong(10, "中转出站交接单汇总"), ZhanDianChuZhanAnDan(11, "站点出站交接单按单"), ZhanDianChuZhanHuiZong(
			12, "站点出站交接单汇总");

	private int value;
	private String text;

	private PrintTemplateOpertatetypeEnum(int value, String text) {
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
