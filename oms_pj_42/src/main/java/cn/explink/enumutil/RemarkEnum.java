package cn.explink.enumutil;

public enum RemarkEnum {
	Damage(1, "入库备注破损"), SuperLarge(2, "入库备注超大"), OverWeight(3, "入库备注超重"), YiPiaoDuoJian(4, "一票多件");

	private int value;
	private String text;

	private RemarkEnum(int value, String text) {
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
