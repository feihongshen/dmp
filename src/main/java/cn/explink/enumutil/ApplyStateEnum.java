package cn.explink.enumutil;

public enum ApplyStateEnum {
	daishenhe(1,"待审核"),
	yishenhe(2,"已审核");
	
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

	private ApplyStateEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
	
	public static String getTextByValue(int value){
		for(ApplyStateEnum ase : ApplyStateEnum.values()){
			if(ase.getValue()==value){
				return ase.getText();
			}
		}
		
		return "";
	}
}
