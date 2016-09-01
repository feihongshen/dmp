package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum DeliveryStateEnum {
	WeiFanKui(0, "未反馈"), 
	PeiSongChengGong(1, "配送成功"), 
	ShangMenTuiChengGong(2, "上门退成功"), 
	ShangMenHuanChengGong(3, "上门换成功"), 
	JuShou(4, "投递失败"), // 拒收修改为投递失败 modify by vic.liang@pjbest.com 2016-08-31
	BuFenTuiHuo(5, "部分拒收"), 
	FenZhanZhiLiu(6, "分站滞留"), 
	ShangMenJuTui(7, "上门拒退"), 
	HuoWuDiuShi(8, "货物丢失"), 
	ZhiLiuZiDongLingHuo(9, "滞留自动领货"), 
	DaiZhongZhuan(10, "待中转");

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
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.PEi_SONG_LEI_XING_WEI_ZHAO_DAO, value);
	}

	public static String getDelayStaes() {
		return "2,3,4,5";
	}
}
