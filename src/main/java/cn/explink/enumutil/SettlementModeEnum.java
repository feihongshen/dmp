package cn.explink.enumutil;

public enum SettlementModeEnum {
	BillMode(1,"账单结算模式"),
	SignRptMode(2,"签收余额模式");
	
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
	private SettlementModeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public static String getTextFromValue(int value){
	            for (SettlementModeEnum mode : SettlementModeEnum.values()) {
	                if (mode.getValue()==value) {
	                    return mode.getText();
	                }
	            }
	     return "";
	}

}
