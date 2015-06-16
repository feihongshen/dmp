package cn.explink.enumutil;

public enum PunishInsideStateEnum {
	daiqueren(1,"待确认"),daishenhe(2,"待审核"),koufachengli(3,"扣罚成立"),koufachexiao(4,"扣罚撤销");
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
