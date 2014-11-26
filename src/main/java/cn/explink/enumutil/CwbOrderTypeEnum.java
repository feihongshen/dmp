package cn.explink.enumutil;

public enum CwbOrderTypeEnum {

	NormalInLibrary(1, "正常入库"), HaveGoodsNoList(2, "有货无单"), HaveListNoGoods(3, "有单无货");

	private int value;
	private String text;

	private CwbOrderTypeEnum(int value, String text) {
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
