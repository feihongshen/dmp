package cn.explink.enumutil;

public enum JiesuanstateEnum {
	ZhengchangJiesuan(1,"正常结算"),
	ZantingJiesuan(2,"暂停结算"),
	TingzhiJiesuan(3,"停止结算");
	
	private int value;
	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	private JiesuanstateEnum(int value, String text){
		this.value = value;
		this.text = text;
	}
	
	/**
	 *  By Comet
	 * @param value
	 * @return
	 */
	public static JiesuanstateEnum getByValue(long value) {
		for (JiesuanstateEnum cc : JiesuanstateEnum.values()) {
			if (value == cc.getValue()) {
				return cc;
			}
		}
		
		return null;
	}
}
