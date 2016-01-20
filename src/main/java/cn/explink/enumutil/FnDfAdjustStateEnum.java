package cn.explink.enumutil;

public enum FnDfAdjustStateEnum {
	YiShengCheng(1,"已生成"),
	WeiShengCheng(2,"未生成");
	
	
	private int value;
	private String text;
	private FnDfAdjustStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	
}
