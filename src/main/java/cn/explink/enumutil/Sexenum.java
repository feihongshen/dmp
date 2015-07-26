package cn.explink.enumutil;

public enum Sexenum {
	Man(1,"男"),
	Woman(2,"女");
	
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
	
	private Sexenum(int value,String text){
		this.value = value;
		this.text = text;
	}
	
}
