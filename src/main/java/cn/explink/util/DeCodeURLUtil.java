package cn.explink.util;

public class DeCodeURLUtil {
	public static String deCodeURLName(String name) {
		String result = null;
		try {
			result = name != null ? new String(name.getBytes("ISO-8859-1"),
					"UTF-8") : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
