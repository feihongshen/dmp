package cn.explink.enumutil;

public enum FlowTimeEnum {
	// ================= sitetype ================
	/*
	 * TiHuo(2,"提货","youjieguoTime"),
	 * TiHuoYouHuoWuDan(3,"提货有货无单","youjieguoTime"),
	 */
	RuKu(4, "入库", "instoreroomtime"), ChuKuSaoMiao(6, "出库", "outstoreroomtime"), FenZhanDaoHuoSaoMiao(7, "到站", "inSitetime"), FenZhanDaoCuoHuoSaoMiao(8, "到错货", "inSitetime"), FenZhanLingHuo(9, "领货",
			"pickGoodstime"), TuiHuoZhanRuKu(15, "退货站入库", "tuihuozhaninstoreroomtime"), TuiGongYingShangChuKu(27, "退供货商出库", "tuigonghuoshangchukutime"), // 新加
	TuiHuoChuZhan(40, "退货出站", "tuihuochuzhantime"), // 新加
	YiFanKui(35, "已反馈", "gobacktime"), YiShenHe(36, "已审核", "goclasstime");

	private int value;
	private String name;
	private String text;

	private FlowTimeEnum(int value, String name, String text) {
		this.value = value;
		this.name = name;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

}
