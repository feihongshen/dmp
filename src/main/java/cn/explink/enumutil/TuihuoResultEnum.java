package cn.explink.enumutil;

public enum TuihuoResultEnum {
	querentuihuo(1,"确认退货"),
	zhandianzhiliu(2,"站点配送"),
	zhandianmaidan(3,"站点买单");
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
	private TuihuoResultEnum(int value, String text){
		this.value = value;
		this.text = text;
	}
}
