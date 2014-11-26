package cn.explink.b2c.dangdang_dataimport;

/**
 * 当当 运单类型枚举
 * 
 * @author Administrator
 *
 */
public enum DangDangOrdertypeEnum {

	PuTongPeiSong(1, "一般运单(配送)"), YouJiHuanHuoDan(2, "邮寄换货运单(上门换)"), ShangMenTuiHuoDan(3, "上门退货运单(上门退)"), ShangMenHuanHuoDan(4, "上门换货运单(换货单)"),

	;
	private int value;
	private String text;

	private DangDangOrdertypeEnum(int value, String text) {
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
