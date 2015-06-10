package cn.explink.enumutil;

public enum ConfirmResultEnum {
	querenbutongguo(1,"确认不通过"),
	querentongguo(2,"确认通过");
	private int value;
	private String txt;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	private ConfirmResultEnum(int value,String txt){
		this.value = value;
		this.txt = txt;
	}
	
}
