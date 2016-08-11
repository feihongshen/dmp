package cn.explink.b2c.dangdang_dataimport;

/**
 * 当当 配送类型 枚举
 * 
 * @author Administrator
 *
 */
public enum DangDangDelivertypeEnum {

	PuTongPeiSong(1, "普通配送"), EMS(2, "EMS"), PingYou(3, "平邮"), JiaJi(5, "加急配送"), DangRidi(101, "当日递"), // 这个单独区分出一个供货商
	ShangMenZiti(11, "上门自提"), KuaiDiGongsiSongHuo(12, "快递公司送货"), SiXiaoShiDi(7, "4小时递"), WanJianSong(13, "晚间送"), 
	guojikuaidi(17,"国际快递"),yuyuesonghuo(18,"预约送货"),dajianpeisong(19,"大件配送")
	;
	private int value;
	private String text;

	private DangDangDelivertypeEnum(int value, String text) {
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
