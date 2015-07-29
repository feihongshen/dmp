package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;


public enum CwbOXOStateEnum {
	UnProcessed(0, "未处理"), Processing(1, "处理中"), Processed(2, "已处理");

	private int value;
	private String text;

	private CwbOXOStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	
	public static CwbOXOStateEnum getByValue(int cwbOXOStateid) {
		for (CwbOXOStateEnum cotie : CwbOXOStateEnum.values()) {
			if (cotie.getValue() == cwbOXOStateid)
				return cotie;
		}
		return null;
	}
	public static String getTextByValue(int value) {
		for (CwbOXOStateEnum cotie : CwbOXOStateEnum.values()) {
			if (cotie.getValue() == value){
				return cotie.getText();
			}	
		}
		return "";
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (CwbOXOStateEnum e : CwbOXOStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
