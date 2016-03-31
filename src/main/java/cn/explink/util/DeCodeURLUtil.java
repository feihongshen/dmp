package cn.explink.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeCodeURLUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DeCodeURLUtil.class);
	
	public static String deCodeURLName(String name) {
		String result = null;
		try {
			result = name != null ? new String(name.getBytes("ISO-8859-1"),
					"UTF-8") : null;
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}
}
