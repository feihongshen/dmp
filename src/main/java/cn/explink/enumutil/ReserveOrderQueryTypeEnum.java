package cn.explink.enumutil;

public enum ReserveOrderQueryTypeEnum {
	
	QUERY("query", "快递预约单查询"),
	HANDLE("handle", "快递预约单操作"),
	WAREHOUSE_HANDLE("warehouseHandle", "快递预约单查询(站点)");
	
	private String value;
	private String text;

	private ReserveOrderQueryTypeEnum(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}
