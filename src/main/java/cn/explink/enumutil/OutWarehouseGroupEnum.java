package cn.explink.enumutil;

public enum OutWarehouseGroupEnum {
	SaoMiaoZhong(1, "扫描中"), FengBao(2, "封包"), YiDaoHuo(3, "已到货"), PaiSongZhong(4, "派送中");

	private int value;
	private String text;

	private OutWarehouseGroupEnum(int value, String text) {
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
