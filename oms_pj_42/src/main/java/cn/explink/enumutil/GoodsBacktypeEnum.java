package cn.explink.enumutil;

public enum GoodsBacktypeEnum {
	Shenhebutongguo(-1, "审核不通过"), Weishenhe(0, "未审核"), Shenweizaitou(1, "审为再投"), Shenweituigongyingshang(2, "审为退供货商"), Gongyishangjushoufanku(3, "供货商拒收返库");

	private int value;
	private String text;

	private GoodsBacktypeEnum(int value, String text) {
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
