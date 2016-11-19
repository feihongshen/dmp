package cn.explink.enumutil;

public enum TmsSaleTypeEnum {
	shangmenhuan_peisong("107","上门换的配送"),
	shangmenhuan_tui("108","上门换的揽退");
	
	private String value;
	private String text;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private TmsSaleTypeEnum(String value,String text){
		this.value = value;
		this.text = text;
	}
}
