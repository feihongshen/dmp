package cn.explink.enumutil;

public enum ConfirmResultEnum {
	querenbutongguo(1,"确认不通过"),
	querentongguo(2,"确认通过");
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
	private ConfirmResultEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
	
}
