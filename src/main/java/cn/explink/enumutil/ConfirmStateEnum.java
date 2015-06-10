package cn.explink.enumutil;

public enum ConfirmStateEnum {
	daiqueren(1,"待确认"),
	yiqueren(2,"已确认");
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
	
	private ConfirmStateEnum(int value,String txt){
		this.value = value;
		this.txt = txt;
	}
}
