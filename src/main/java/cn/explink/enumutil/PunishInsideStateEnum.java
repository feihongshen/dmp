package cn.explink.enumutil;

public enum PunishInsideStateEnum {
	chuangjian(1,"对内扣罚单创建"),koufadanshensu(2,"扣罚单申诉"),koufadanshenhe(3,"扣罚单审核");
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
	private PunishInsideStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
}
