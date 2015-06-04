package cn.explink.enumutil;

public enum ShenHeResultEnum {
	shenhebutongguo(1,"审核不通过"),
	shenhetongguo(2,"审核通过");
	
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
	private ShenHeResultEnum(int vlaue,String text){
		this.value = value;
		this.text = text;
	}
}
