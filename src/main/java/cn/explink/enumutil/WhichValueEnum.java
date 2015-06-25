package cn.explink.enumutil;

public enum WhichValueEnum {
	Gudingxiang(0,"固定项"),
	Linshixiang(1,"临时项");
	private int value;
	private String text;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private WhichValueEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
}
