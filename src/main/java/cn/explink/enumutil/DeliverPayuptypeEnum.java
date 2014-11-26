package cn.explink.enumutil;

public enum DeliverPayuptypeEnum {
	// 0非小件员交款 1网银（网银需要小票号） 2POS 3现金
	FeiXiaoJianYuanJiaoKuan(0, "非小件员交款"), WangYin(1, "网银"), Pos(2, "POS"), XianJin(3, "现金");

	private int value;
	private String text;

	private DeliverPayuptypeEnum(int value, String text) {
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
