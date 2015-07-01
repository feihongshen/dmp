package cn.explink.enumutil;

public enum BillStateEnum {
	
	WeiShenHe(1,"未审核"),YiShenHe(2,"已审核"),YiHeXiao(3,"已核销");
	
	private long value;
	private String text;
	private BillStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public static BillStateEnum getByValue(int cwbordertypeid) {
		for (BillStateEnum cotie : BillStateEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
	public static String getTextByValue(int value) {
		for (BillStateEnum cotie : BillStateEnum.values()) {
			if (cotie.getValue() == value){
				return cotie.getText();
			}	
		}
		return "";
	}
		

}
