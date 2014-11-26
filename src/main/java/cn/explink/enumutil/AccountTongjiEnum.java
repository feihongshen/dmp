package cn.explink.enumutil;

public enum AccountTongjiEnum {
	ZhongZhuanFees(1, "中转退款金额"), ZhongZhuanCwbs(2, "中转退款订单号"), ZhongZhuanNums(3, "中转退款订单数"), TuiHuoFees(4, "退货退款金额"), TuiHuoCwbs(5, "退货退款订单号"), TuiHuoNums(6, "退货退款订单数"), JiaKuan(7, "加款"), JianKuan(8,
			"减款"), JiaKuanCwbs(23, "加款id号"), JianKuanCwbs(24, "减款id号"), YingJiao(9, "应交/已交"), YingJiaoNums(11, "应交货款订单数"), QianKuan(10, "欠款"), YingJiaoCash(12, "已妥投交款现金"), YingJiaoPos(13, "已妥投交款POS"), YingJiaoCheck(
			14, "已妥投交款支票"), YingJiaoOther(15, "已妥投交款其他"), YingJiaoEjCash(16, "二级已妥投交款现金"), YingJiaoEjPos(17, "二级已妥投交款POS"), YingJiaoEjCheck(18, "二级已妥投交款支票"), YingJiaoEjOther(19, "二级已妥投交款其他"), YingJiaoEjNums(
			20, "二级乙妥投交款订单数"), WeiJiao(21, "未交"), ToYingJiao(22, "总计应交"), JiaKuanNums(25, "加款单数"), JianKuanNums(26, "减款单数"), QianKuanNums(27, "欠款订单数"), QianKuanCwbs(28, "欠款订单号"), PosFees(29,
			"POS退款金额"), PosNums(30, "POS退款订单数"), PosCwbs(31, "POS退款订单号");

	private long value;
	private String text;

	private AccountTongjiEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
