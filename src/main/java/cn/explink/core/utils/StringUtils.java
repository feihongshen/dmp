package cn.explink.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 字符串工具类（继承org.apache.commons.lang3.StringUtils类）
 * 
 * @author gaoll
 * 
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	private static Logger logger =LoggerFactory.getLogger(StringUtils.class);
	
	public static String lowerFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toLowerCase() + str.substring(1);
		}
	}
	
	public static String upperFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return "";
	}

	/**
	 * 缩略字符串（替换html）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
        return abbr(replaceHtml(str), length);
	}
		
	
	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}
	
	/**
	 * 去除
	 * @param str
	 * @return
	 */
	public static String getRealString(String str) {
		
		String reg = "[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		String beizhu = m.replaceAll("");
		return beizhu;
	}
	
    /** 
     * 判断字符串是否为空或空字符串 
     * @param str 原字符串 
     * @return 
     */  
    public static boolean isEmpty(String str) {  
        return str == null || "".equals(str);  
    }  
  
    /** 
     * 全角转半角: 
     * @param fullStr 
     * @return 
     */  
    public static final String full2Half(String fullStr) {  
        if(isEmpty(fullStr)){  
            return fullStr;  
        }  
        char[] c = fullStr.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] >= 65281 && c[i] <= 65374) {  
                c[i] = (char) (c[i] - 65248);  
            } else if (c[i] == 12288) { // 空格  
                c[i] = (char) 32;  
            }  
        }  
        return new String(c);  
    }  
  
    /** 
     * 半角转全角 
     * @param halfStr 
     * @return 
     */  
    public static final String half2Full(String halfStr) {  
        if(isEmpty(halfStr)){  
            return halfStr;  
        }  
        char[] c = halfStr.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 32) {  
                c[i] = (char) 12288;  
            } else if (c[i] < 127) {  
                c[i] = (char) (c[i] + 65248);  
            }  
        }  
        return new String(c);  
    }  
    
    public static void main(String[] args) {
		logger.info(half2Full("你好，再见123abc"));
	}
}
