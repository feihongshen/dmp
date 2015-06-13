package cn.explink.enumutil;

public enum ShenHeStateEnum {
	daishenhe(1,"待审核"),
	shenhebutongguo(2,"审核不通过"),
	shenhetongguo(3,"审核通过");
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
	private ShenHeStateEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
}
