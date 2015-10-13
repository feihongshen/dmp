package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum ExpressSysMonitorEnum {

	TMALL(-600000, "tmall"), DANGDANG(-600000, "dangdang"), AMAZON(-600000, "amazon"), GOME(-600000, "gome"), FANKE(-600000, "fanke"), YIHAODIAN(-600000, "yihaodian");
	private int value;
	private String text;

	private ExpressSysMonitorEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}

	private static final Map<String, ExpressSysMonitorEnum> stringToEnum = new HashMap<String, ExpressSysMonitorEnum>();
	static {
		// Initialize map from constant name to enum constant
		for (ExpressSysMonitorEnum blah : values()) {
			stringToEnum.put(blah.toString(), blah);
		}
	}

	// Returns Blah for string, or null if string is invalid
	public static ExpressSysMonitorEnum fromString(String symbol) {
		return stringToEnum.get(symbol);
	}
}
