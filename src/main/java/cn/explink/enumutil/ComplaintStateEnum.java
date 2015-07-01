package cn.explink.enumutil;

import cn.explink.exception.ExplinkException;

public enum ComplaintStateEnum {
	DaiHeShi(1,"待核实"),YiHeShi(2,"已核实"),YiJieAn(3, "已结案");

	private int value;
	private String text;

	private ComplaintStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}


	public static ComplaintStateEnum getByValue1(int value) {

		for (ComplaintStateEnum ct : ComplaintStateEnum.values()) {
			if (ct.getValue() == value) {
				return ct;
			}
		}
		return null;
	}
	
	public static String getByValue(int value) {
		String complainstate="";
		for (ComplaintStateEnum ct : ComplaintStateEnum.values()) {
			if (ct.getValue() == value) {
				complainstate=ct.getText();
			}
		}
		return complainstate;
	}
	
	
}
