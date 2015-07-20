package cn.explink.enumutil;

public enum SequenceEnum {
	ShengXu("ASC","升序"),JiangXu("DESC","降序");
	private String value;
	private String text;
	
	private SequenceEnum(String value,String text){
		this.value=value;
		this.text=text;
	}
	
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
}
