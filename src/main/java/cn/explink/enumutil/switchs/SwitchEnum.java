package cn.explink.enumutil.switchs;

public enum SwitchEnum {
	ChuKuYunXu("出库", "ck_01"), ChuKuBuYunXu("出库", "ck_02"), FanKui("单票反馈", "dpfk_01"), FanKuiBuYunXu("单票反馈", "dpfk_02"), RuKuDaYinBiaoQian("入库打印标签", "rkbq_01"), RuKuBuDaYinBiaoQian("入库打印标签",
			"rkbq_02"), RuKuDaYinnewBiaoQian("入库打印标签", "rkbq_03"), DaoRuShuJuChuangJianFaHuoCangKu("导入数据创建发货仓库", "cjfhck_01"), DaoRuShuJuBuChuangJianFaHuoCangKu("导入数据创建发货仓库", "cjfhck_02"), PiLiangFanKuiPOS(
			"批量反馈支付方式pos", "plzffs_01"), PiLiangFanKuiNoPOS("批量反馈支付方式pos", "plzffs_02"), ChuKuFengBao("出库封包", "ckfb_01"), ChuKuBuFengBao("出库封包", "ckfb_02"), DaoHuoFengBao("分站到货封包", "fzdhfb_01"), DaoHuoBuFengBao(
			"分站到货封包", "fzdhfb_02"), PiLiangFanKui("批量反馈", "plfk_01"), PiLiangFanKuiNo("批量反馈", "plfk_02");

	private String text;
	private String info;

	private SwitchEnum(String text, String info) {
		this.text = text;
		this.info = info;
	}

	public String getText() {
		return text;
	}

	public String getInfo() {
		return info;
	}
}
