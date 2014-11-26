package cn.explink.b2c.homegobj;

public enum FunctionEnum {

	JIAYOU0001("JIAYOU0001", "订单查询"), JIAYOU0002("JIAYOU0002", "订单状态"), JIAYOU0003("JIAYOU0003", "订单轨迹"),

	;

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String text;

	private FunctionEnum(String key, String text) {
		this.key = key;
		this.text = text;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
