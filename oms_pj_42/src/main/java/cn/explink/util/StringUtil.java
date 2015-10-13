package cn.explink.util;

public class StringUtil {
	public static String nullConvertToEmptyString(String string) {
		String lastusername = string == null ? "" : string;
		return lastusername;
	}

	public static String nullConvertToEmptyString(Object string) {
		String lastusername = string == null ? "" : (String) string;
		return lastusername;
	}
}
