package cn.explink.enumutil;

public enum ResultStateEnum {
	success(1,"成功"),
	jushou(2,"拒收");
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
	
	private ResultStateEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
}
