package cn.explink.enumutil;

public enum ConfirmStateEnum {
	daiqueren(1,"待确认"),
	yiqueren(2,"已确认");
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
	private ConfirmStateEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
}
