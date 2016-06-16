package cn.explink.core.utils;

import javax.servlet.http.HttpServletRequest;

public final class WebUtils {

	
	public static String getStringParam(HttpServletRequest req, String paramName, String defaultVal){
		String param = req.getParameter(paramName);
		if( param != null){
			return param;
		}else{
			return defaultVal;
		}
	}
	
	public static Integer getIntParam(HttpServletRequest req, String paramName, Integer defaultVal){
		String param = req.getParameter(paramName);
		if( param != null){
			return Integer.valueOf(param);
		}else{
			return defaultVal;
		}
	}
	
	public static Long getLongParam(HttpServletRequest req, String paramName, Long defaultVal){
		String param = req.getParameter(paramName);
		if( param != null){
			return Long.valueOf(param);
		}else{
			return defaultVal;
		}
	}
}
