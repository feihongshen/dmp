package cn.explink.enumutil;

public enum CwbOrderListTypeEnum {
	ZHAN_DIAN_JIAN_KONG_ZAI_TU(1, "站点监控-在途"), ZHAN_DIAN_JIAN_KONG_KU_CUN(2, "站点监控-库存"), ZHAN_DIAN_JIAN_KONG_PAI_SONG_ZHONG(3, "站点监控-派送中");

	private int value;
	private String text;

	private CwbOrderListTypeEnum(int value, String text) {
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
