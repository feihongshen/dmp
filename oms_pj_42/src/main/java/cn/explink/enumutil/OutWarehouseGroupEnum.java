package cn.explink.enumutil;

public enum OutWarehouseGroupEnum {
	SaoMiaoZhong(0, "扫描中"), FengBao(1, "封包"), YiDaoHuo(2, "已到货"), PaiSongZhong(3, "派送中");

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
