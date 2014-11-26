package cn.explink.enumutil;

public enum BaleStateEnum {
	SaoMiaoZhong(0, "扫描中"), WeiDaoZhan(1, "未到站"), YiRuKu(2, "已入库"), BaoDiuShi(3, "包丢失"), FeiBenZhanBao(4, "非本站包"), KeYong(6, "可用"), BuKeYong(7, "不可用"),

	YiDaoHuo(5, "已到货"), WeiFengBao(8, "未封包"), YiFengBao(9, "已封包"), // 已封包未出库
	YiFengBaoChuKu(10, "已封包出库");// 已封包已出库

	private int value;
	private String text;

	private BaleStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static BaleStateEnum getValue(long value) {
		for (BaleStateEnum typeEnum : BaleStateEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}
