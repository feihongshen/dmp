package cn.explink.enumutil;
/*反馈日期，发货日期，入库日期，审核日期*/
public enum CwbDateEnum {
	ShenHeRiQi(1,"审核日期"),FaHuoRiQi(2,"发货日期"),RUKuRiQi(3,"入库日期"),FanKuiRiQi(4,"反馈日期");

	private long value;
	private String text;
	private CwbDateEnum(int value, String text) {
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
	
	public static CwbDateEnum getByValue(int cwbordertypeid) {
		for (CwbDateEnum cotie : CwbDateEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
	public static String getTextByValue(int value) {
		for (CwbDateEnum cotie : CwbDateEnum.values()) {
			if (cotie.getValue() == value){
				return cotie.getText();
			}	
		}
		return "";
	}
}
