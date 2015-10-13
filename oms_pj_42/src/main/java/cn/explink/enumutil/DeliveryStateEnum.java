package cn.explink.enumutil;

public enum DeliveryStateEnum {
	PeiSongChengGong(1, "配送成功"), ShangMenTuiChengGong(2, "上门退成功"), ShangMenHuanChengGong(3, "上门换成功"), QuanBuTuiHuo(4, "拒收"), BuFenTuiHuo(5, "部分拒收"), ShangMenJuTui(7, "上门拒退"), HuoWuDiuShi(8, "货物丢失"), FenZhanZhiLiu(
			6, "分站滞留"), ZhiLiuZiDongLingHuo(9, "滞留自动领货"), WeiFanKui(0, "未反馈");

	private int value;
	private String text;

	private DeliveryStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static DeliveryStateEnum getByValue(int value) {
		for (DeliveryStateEnum deliveryStateEnum : DeliveryStateEnum.values()) {
			if (value == deliveryStateEnum.getValue()) {
				return deliveryStateEnum;
			}
		}
		return null;
	}
}
