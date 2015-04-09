package cn.explink.enumutil;

public enum VerificationEnum {
	YiHeXiao(1,"已核销"),WeiHeXiao(2,"未核销");
	private int value;
	private String text;
	private VerificationEnum(int value, String text) {
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
