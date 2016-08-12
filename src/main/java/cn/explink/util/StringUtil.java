package cn.explink.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	
	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String nullConvertToEmptyString(String string) {
		String lastusername = string == null ? "" : string;
		if ("null".equals(lastusername)) {
			lastusername = "";
		}
		return lastusername;
	}

	public static String nullConvertToEmptyString(Object string) {
		String lastusername = string == null ? "" : (String) string;
		return lastusername;
	}

	public static BigDecimal nullConvertToBigDecimal(Object bd) {
		BigDecimal lastusername = bd == null ? BigDecimal.ZERO : (BigDecimal) bd;
		return lastusername;
	}

	public static Long nullConvertToLong(Object bd) {
		Long lastusername = bd == null ? Long.valueOf(0) : (Long) bd;
		return lastusername;
	}

	public static java.util.Date formateSqlDateTime(Timestamp time) {
		java.util.Date date = null;
		if (time != null) {
			String timeStr = time.toString();
			String dateStr = timeStr.substring(0, timeStr.length() - 2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		return date;
	}

	public static String nullDateConverToEmptyString(Object bd) {
		if (bd == null) {
			return "";
		} else {
			if (bd instanceof Date) {
				return StringUtil.sdf.format(bd);
			} else if (bd instanceof java.util.Date) {
				return StringUtil.sdf.format(bd);
			} else {
				return bd.toString();
			}
		}
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 *
	 * @param c
	 *            需要判断的字符
	 * @return 返回true,Ascill字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return (c / k) == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 *
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!StringUtil.isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
	 *
	 *
	 * @param origin
	 *            原始字符串
	 * @param len
	 *            截取长度(一个汉字长度按2算的)
	 * @param c
	 *            后缀
	 * @return 返回的字符串
	 */
	public static String substring(String origin, int len, String c) {
		if ((origin == null) || origin.equals("") || (len < 1)) {
			return "";
		}
		byte[] strByte = new byte[len];
		if (len > StringUtil.length(origin)) {
			return origin + c;
		}
		try {
			System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if ((count % 2) != 0) {
				len = (len == 1) ? ++len : --len;
			}
			return new String(strByte, 0, len, "GBK") + c;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按字节数获取子字符串
	 *
	 * @param str
	 *            原字符串
	 * @param beginindex
	 *            开始位置
	 * @param endindex
	 *            结束位置
	 * @return 子字符串
	 */

	public static String subStrByByte(String str, int beginindex, int endindex, String charset) {
		String result = "";
		int charLength = 0;
		int tempIndex1 = 0;
		int tempIndex2 = 0;
		int charBeginIndex = -1;
		int charEndIndex = -1;

		if ((endindex > beginindex) && (beginindex >= 0)) {
			for (int i = 0; i < str.length(); i++) {
				try {

					if ((charset == null) || charset.isEmpty()) {
						charLength = str.substring(i, i + 1).getBytes().length;
					} else {
						charLength = str.substring(i, i + 1).getBytes(charset).length;
					}
				} catch (UnsupportedEncodingException e) {
					logger.error("", e);
				}
				tempIndex1 = tempIndex2;
				tempIndex2 += charLength;

				if ((beginindex >= tempIndex1) && (beginindex < tempIndex2)) {
					charBeginIndex = i;
				}

				if ((endindex >= tempIndex1) && (endindex < tempIndex2)) {
					charEndIndex = endindex == tempIndex1 ? i : i + 1;
					break;
				}
			}

			charEndIndex = charEndIndex == -1 ? (charBeginIndex == -1 ? 0 : str.length()) : charEndIndex;
			charBeginIndex = charBeginIndex == -1 ? 0 : charBeginIndex;
			result = str.substring(charBeginIndex, charEndIndex);
		}

		return result.trim();
	}

	/**
	 * 按字节数获取子字符串
	 *
	 * @param str
	 *            原字符串
	 * @param beginindex
	 *            开始位置
	 * @param endindex
	 *            结束位置
	 * @return 子字符串
	 */

	public static String subStrByByteNoEncode(String str, int beginindex, int endindex, String charset) {
		String result = "";
		int charLength = 0;
		int tempIndex1 = 0;
		int tempIndex2 = 0;
		int charBeginIndex = -1;
		int charEndIndex = -1;

		if ((endindex > beginindex) && (beginindex >= 0)) {
			for (int i = 0; i < str.length(); i++) {
				charLength = str.substring(i, i + 1).getBytes().length;

				tempIndex1 = tempIndex2;
				tempIndex2 += charLength;

				if ((beginindex >= tempIndex1) && (beginindex < tempIndex2)) {
					charBeginIndex = i;
				}

				if ((endindex >= tempIndex1) && (endindex < tempIndex2)) {
					charEndIndex = endindex == tempIndex1 ? i : i + 1;
					break;
				}
			}

			charEndIndex = charEndIndex == -1 ? (charBeginIndex == -1 ? 0 : str.length()) : charEndIndex;
			charBeginIndex = charBeginIndex == -1 ? 0 : charBeginIndex;
			result = str.substring(charBeginIndex, charEndIndex);
		}

		return result.trim();
	}

	/**
	 * 返回String 数组 的toString
	 *
	 * @param strS
	 *            数组
	 * @return toString
	 */
	public static String getStringsToString(String[] strS) {
		StringBuffer reStr = new StringBuffer();
		String s = ",";
		for (String str : strS) {
			reStr = reStr.append(str).append(s);
		}
		if (reStr.length() > 0) {
			return reStr.substring(0, reStr.length() - 1);
		} else {
			return reStr.toString();
		}
	}

	/**
	 * 按字节数获取子字符串
	 *
	 * @param str
	 *            原字符串
	 * @param beginindex
	 *            开始位置
	 * @param endindex
	 *            结束位置
	 * @return 子字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String subStrByByteEncoding(String str, int beginindex, int endindex, String encoding) {

		try {
			if (endindex <= beginindex) {
				return "";
			}
			if (beginindex < 0) {
				return "";
			}
			byte[] bytes;

			bytes = str.getBytes(encoding);

			byte[] resultbytes = new byte[endindex - beginindex];
			for (int i = beginindex; i < endindex; i++) {
				resultbytes[i - beginindex] = bytes[i - 1];
			}

			return new String(resultbytes, encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 按字节数获取子字符串
	 *
	 * @param str
	 *            原字符串
	 * @param beginindex
	 *            开始位置
	 * @param endindex
	 *            结束位置
	 * @return 子字符串
	 */

	public static String subString(String str, int beginindex, int endindex) {

		return str.substring(beginindex, endindex);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		String str1 = "1,2,3,4,5,6";
		String str2 = "5,6,7,8";

		logger.info(StringUtil.removalDuplicateString(str1, str2));
	}

	public static String getStringsByLongList(List<Long> strArr) {
		String strs = "";
		if (strArr.size() > 0) {
			for (Long str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public static String getStringsByStringList(List<String> strArr) {
		String strs = "";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	/**
	 * @author Zhangjianbao 把时间格式字符串转换成Quartz 日期格式(精确到时分秒) <<10:15:00>> to << 0
	 *         15 10 * * ? * >>
	 * @param time
	 * @return
	 */
	public static String getTimeToQuartzHHMMss(String time) {
		return StringUtil.reverseSort(time).replaceAll(":", " ") + " * * ? *";
	}

	/**
	 * @author Zhangjianbao 字符串倒序输出 <<abc>> to <<cba>>
	 * @param str
	 * @return
	 */
	public static String reverseSort(String str) {
		String str2 = "";
		for (int i = str.length() - 1; i > -1; i--) {
			str2 += String.valueOf(str.charAt(i));
		}

		return str2;
	}

	public static String removalDuplicateString(String strs) {
		List list = Arrays.asList(strs.split(","));
		Set set = new HashSet(list);
		String[] strArr = (String[]) set.toArray(new String[0]);
		String rtnStr = StringUtil.getStringsToString(strArr);
		return rtnStr;
	}

	public static String removalDuplicateString(String srcStr, String destStr) {
		List<String> list = Arrays.asList(srcStr.split(","));
		List<String> rtnList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (destStr.indexOf(list.get(i)) < 0) {
				rtnList.add(list.get(i));
			}
		}
		String rtnStr = StringUtils.join(rtnList, ",");
		return rtnStr;
	}

	public static BigDecimal nullOrEmptyConvertToBigDecimal(String bd) {
		BigDecimal lastusername = ((bd == null) || ((null != bd) && bd.equals(""))) ? BigDecimal.ZERO : new BigDecimal(bd);
		return lastusername;
	}

	/**
	 *
	 * 根据正则表达式分割字符串
	 *
	 * @param str
	 *            源字符串
	 * @param ms
	 *            正则表达式
	 * @return 目标字符串组
	 */
	public static String[] splitString(String str, String ms) {
		String regEx = ms;
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		String[] sp = p.split(str);
		return sp;
	}

	/**
	 * 字符串判空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public static String nullOrEmptyStringConvertToEmpty(Object string) {
		String lastusername = string == null ? "空" : (String) string;
		if ("".equals(lastusername)) {
			lastusername = "空";
		}
		return lastusername;
	}
	
	/**
	 * 数组转换层数据库in字段
	 * @date 2016年8月11日 下午3:45:48
	 * @param objs
	 * @return
	 */
	public static String toDbInStr(String[] objs) {
		StringBuilder objSb = new StringBuilder("");
		for (String obj : objs) {
			if (obj != null && obj.length() > 0) {
				objSb.append("'").append(obj).append("'").append(",");
			}
		}
		String objStr = objSb.toString();
		if (objStr.endsWith(",")) {
			objStr = objStr.substring(0, objStr.length() - 1);
		}
		return objStr;
	}
	
	/**
	 * 数组转换层数据库in字段
	 * @date 2016年8月11日 下午3:45:48
	 * @param objs
	 * @return
	 */
	public static String toDbInStr(long[] objs) {
		StringBuilder objSb = new StringBuilder("");
		for (long obj : objs) {
			objSb.append(obj).append(",");
		}
		String objStr = objSb.toString();
		if (objStr.endsWith(",")) {
			objStr = objStr.substring(0, objStr.length() - 1);
		}
		return objStr;
	}
	
	/**
	 * 数组转换层数据库in字段
	 * @date 2016年8月11日 下午3:45:48
	 * @param objs
	 * @return
	 */
	public static String toDbInStr(int[] objs) {
		StringBuilder objSb = new StringBuilder("");
		for (long obj : objs) {
			objSb.append(obj).append(",");
		}
		String objStr = objSb.toString();
		if (objStr.endsWith(",")) {
			objStr = objStr.substring(0, objStr.length() - 1);
		}
		return objStr;
	}
}
