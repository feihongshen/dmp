package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;


public enum CwbOXOTypeEnum {
	pick(1, "揽收"), delivery(2, "配送");

	private int value;
	private String text;

	private CwbOXOTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	
	public static CwbOXOTypeEnum getByValue(int cwbOXOStateid) {
		for (CwbOXOTypeEnum cotie : CwbOXOTypeEnum.values()) {
			if (cotie.getValue() == cwbOXOStateid)
				return cotie;
		}
		return null;
	}
	public static String getTextByValue(int value) {
		for (CwbOXOTypeEnum cotie : CwbOXOTypeEnum.values()) {
			if (cotie.getValue() == value){
				return cotie.getText();
			}	
		}
		return "";
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (CwbOXOTypeEnum e : CwbOXOTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
