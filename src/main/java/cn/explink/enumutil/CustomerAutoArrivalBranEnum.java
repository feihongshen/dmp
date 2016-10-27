package cn.explink.enumutil;

/*
 * 客户是否开启揽退单自动到货枚举---刘武强20161026
 */
public enum CustomerAutoArrivalBranEnum {
	bukaiqi(0,"不开启"), kaiqi(1,"开启");
	
	private int value;
	private String text;
	
	private CustomerAutoArrivalBranEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
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
	
}
