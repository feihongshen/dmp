package cn.explink.enumutil;

public enum BranchEnum {
	// ================= sitetype ================
	KuFang(1, "库房"), ZhanDian(2, "站点"), TuiHuo(3, "退货"), ZhongZhuan(4, "中转"), KeFu(5, "客服"), YunYing(6, "运营"), CaiWu(7, "财务"), QiTa(8, "其他"),
	// ================= sitetype end================
	// ================= 机构权限设置 ================
	BuQiYong(16, "不启用"), TiaoMaDaYin(17, "条码打印"), YuYinTiXing(18, "语音提醒"), YuYinAndTiaoMa(19, "语音和条码");
	// ================= 机构权限设置 end ================
	private int value;
	private String text;

	private BranchEnum(int value, String text) {
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
