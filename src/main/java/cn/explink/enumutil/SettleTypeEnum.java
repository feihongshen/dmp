package cn.explink.enumutil;

import cn.explink.util.Tools;

public enum SettleTypeEnum {

	pay(1,"买单结算"),
	repayment(2,"返款结算");
	
	private Integer value;
	private String text;

	private SettleTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	
	public static SettleTypeEnum getByValue(Integer code) {
		for (SettleTypeEnum cotie : SettleTypeEnum.values()) {
			if (cotie.getValue() == code)
				return cotie;
		}
		return null;
	}
	
	public static Integer convertText2Value(String text){
		if(!Tools.isEmpty(text)){
			for (SettleTypeEnum cotie : SettleTypeEnum.values()) {
				if (cotie.getText().equals(text))
					return cotie.getValue();
			}
		}
		return 0;
	}
	
	//根据code获得字符串
	public static String getTextByValue(Integer code) {
		for (SettleTypeEnum cotie : SettleTypeEnum.values()) {
			if (cotie.getValue() == code)
				return cotie.getText();
		}
		return null;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
