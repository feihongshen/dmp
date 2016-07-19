package cn.explink.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.explink.pos.tools.JacksonMapper;
	
public class Tools {
	public final static int DB_OPERATION_MAX = 10000;
	static ObjectMapper outMapper = JacksonMapper.getInstance();
	static ObjectMapper mapper = JacksonMapper.getInstance();
	public static String SPLIT_PATTERN = ",|;|，|；|(\\n)";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	/**
	 * 替换临时变量
	 */
	private static Pattern pIndex = Pattern.compile("\\[(.*?)\\]");
	static int currNoTimes = 0;

	private static Logger logger = LoggerFactory.getLogger(Tools.class);
	
	// 获取默认14位数字+前缀字符
	public static String getRandomSeq(String prefix) {
		return Tools.getRandomSeq(prefix, 1).get(0);
	}

	/**
	 *
	 * 获取默认14位数字+前缀字符 获取随机不重复编号
	 *
	 * @param prefix
	 *            前缀
	 * @param num
	 *            获取数量<=1000
	 * @return 列表
	 */
	public synchronized static List<String> getRandomSeq(String prefix, int num) {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
		String pre = prefix + df.format(new Date());

		List<String> result = new ArrayList<String>();
		if (num > 1) {
			for (int i = 0; i <= Math.min(num - 1, 999); i++) {
				result.add(pre + Tools.num2str(Tools.currNoTimes, 3));
				Tools.currNoTimes++;
				if (Tools.currNoTimes >= 1000) {
					Tools.currNoTimes = 0;
				}
			}
		} else {
			result.add(pre + Tools.num2str(Tools.currNoTimes, 3));
			Tools.currNoTimes++;
			if (Tools.currNoTimes >= 1000) {
				Tools.currNoTimes = 0;
			}
		}

		return result;
	}

	/**
	 *
	 * 获取当前时间字符串
	 *
	 * @param patten
	 *            时间格式，NULL为标准日期时间
	 * @return
	 */
	public static String getCurrentTime(String patten) {
		if (patten == null) {
			patten = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(new java.util.Date());
	}

	/**
	 *
	 * 格式化时间字符串
	 *
	 * @param patten
	 *            时间格式，NULL为标准日期时间
	 * @return
	 */
	public static String formatTime(Date date, String patten) {
		if (patten == null) {
			patten = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(date);
	}

	/**
	 * 按char型分隔符分割字符,将所给分隔符变成拆分成多个char,然后分别分割
	 *
	 * @param str
	 *            原始字符
	 * @param spec
	 *            分隔
	 * @return 分割后的字符数组
	 */
	public static String[] strSplit(String str, String spec) {
		return Tools.strSplit(str, spec, false);
	}

	/**
	 * 按char型分隔符分割字符,将所给分隔符变成拆分成多个char,然后分别分割
	 *
	 * @param str
	 *            原始字符
	 * @param spec
	 *            分隔
	 * @param withNull
	 *            是否统计空字符串
	 * @return 分割后的字符数组
	 */
	public static String[] strSplit(String str, String spec, boolean withNull) {
		if (Tools.isEmpty(str)) {
			return new String[0];
		}
		StringTokenizer token = new StringTokenizer(str, spec);
		int count = token.countTokens();
		Vector<String> vt = new Vector<String>();
		for (int i = 0; i < count; i++) {
			String tmp = token.nextToken();
			if (withNull || !Tools.isEmpty(tmp)) {
				vt.addElement(tmp.trim());
			}
		}
		return vt.toArray(new String[0]);
	}

	/**
	 * 替换字符
	 *
	 * @param str
	 *            原是字符
	 * @param pattern
	 *            被替换的字符
	 * @param replace
	 *            要替换成的字
	 * @return 替换后的字符
	 */
	public static String replace(String str, String pattern, String replace) {
		if (str == null) {
			return null;
		}
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	/**
	 * 按分隔符分割字符
	 *
	 * @param str
	 *            原始字符
	 * @param spec
	 *            分隔
	 * @return 分割后的字符数组
	 */
	public static String[] str2array(String str, String spec) {
		return Tools.str2array(str, spec, false);
	}

	/**
	 * 按分隔符分割字符
	 *
	 * @param str
	 *            原始字符
	 * @param spec
	 *            分隔
	 * @param withNull
	 *            是否统计空字符串
	 * @return 分割后的字符数组
	 */
	public static String[] str2array(String str, String spec, boolean withNull) {
		if (Tools.isEmpty(str) && !withNull) {
			return new String[0];
		}
		if (Tools.isEmpty(str) && withNull) {
			return new String[] { "" };
		}
		List<String> vt = Tools.str2List(str, spec, withNull);
		String[] ar = vt.toArray(new String[0]);
		return ar;
	}

	/**
	 * 按分隔符分割字符
	 *
	 * @param str
	 *            原始字符
	 * @param spec
	 *            分隔
	 * @param withNull
	 *            是否统计空字符串
	 * @return 分割后的字符链表
	 */
	public static List<String> str2List(String str, String spec, boolean withNull) {
		if (str == null) {
			str = "";
		}
		Vector<String> vt = new Vector<String>();
		while (str.indexOf(spec) != -1) {
			String tmp = str.substring(0, str.indexOf(spec));
			if (withNull || !tmp.equals("")) {
				vt.addElement(tmp.trim());
			}
			str = str.substring(str.indexOf(spec) + spec.length());
		}
		if (withNull || !Tools.isEmpty(str.trim())) {
			vt.addElement(str.trim());
		}
		return vt;
	}

	/**
	 * 判断个字符是否为
	 *
	 * @param source
	 *            要判断字符串
	 * @return 如果为空返回真，反之为假
	 */
	public static boolean isEmpty(String source) {
		return ((source == null) || source.trim().equals(""));
	}

	/**
	 * 判断个对象是否为
	 *
	 * @param source
	 *            要判断对
	 * @return 如果为空返回真，反之为假
	 */
	public static boolean isEmpty(Object source) {
		return ((source == null) || source.toString().trim().equals(""));
	}

	public static boolean isEmpty(String[] source) {
		return ((source == null) || (source.length <= 0));
	}
	
	/**
	 * add by 周欢   2016-07-15
	 * 判断个字符是否为,如果为空则返回默认值
	 * @param source要判断字符串
	 * @param value要判断字符串
	 * @return 如果为空返回期望值
	 */
	public static String dealEmptyValue(String source,String value) {
		if((source == null) || source.trim().equals("")){
			return value;
		}else{;
			return source;
		}
	}

	/**
	 * 获取输入流内容变成字节流
	 *
	 * @param is
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromInputStream(InputStream is, int length) throws IOException {
		if (is == null) {
			return null;
		}
		if (length < 0) {
			length = 0;
		}
		byte[] allBytes = new byte[length];
		DataInputStream in = null;
		in = new DataInputStream(is);

		int bytesRead = 0;
		int totalBytesRead = 0;
		int sizeCheck = 0;
		if (length > 0) {
			while (bytesRead != -1) {
				byte[] readBytes = new byte[8192];
				bytesRead = in.read(readBytes);
				if (bytesRead != -1) {
					totalBytesRead += bytesRead;

					if (totalBytesRead <= length) {
						System.arraycopy(Tools.midBytes(readBytes, 0, bytesRead), 0, allBytes, totalBytesRead - bytesRead, bytesRead);
					} else {
						sizeCheck = totalBytesRead - length;
						System.arraycopy(Tools.midBytes(readBytes, 0, bytesRead), 0, allBytes, totalBytesRead - bytesRead, bytesRead - sizeCheck);
						break;
					}
				}
			}
			in.close();

			if (totalBytesRead < length) {
				return Tools.midBytes(allBytes, 0, totalBytesRead);
			} else {
				return allBytes;
			}
		}
		// 读未知长度数据流
		length = 2048 * 1024;
		allBytes = new byte[length];
		while (bytesRead != -1) {
			byte[] readBytes = new byte[8192];
			bytesRead = in.read(readBytes);
			if (bytesRead != -1) {
				totalBytesRead += bytesRead;
				if (totalBytesRead <= length) {
					System.arraycopy(Tools.midBytes(readBytes, 0, bytesRead), 0, allBytes, totalBytesRead - bytesRead, bytesRead);
				} else {
					length += 2048 * 1024;
					byte[] tmpBytes = allBytes;
					allBytes = new byte[length];
					System.arraycopy(tmpBytes, 0, allBytes, 0, tmpBytes.length);
					System.arraycopy(Tools.midBytes(readBytes, 0, bytesRead), 0, allBytes, totalBytesRead - bytesRead, bytesRead - sizeCheck);
				}
			}
		}
		in.close();
		is.close();
		return Tools.midBytes(allBytes, 0, totalBytesRead);
	}

	public static byte[] getFileBytes(File file1) throws IOException {
		if (!file1.exists()) {
			return null;
		}
		FileInputStream fileinputstream = new FileInputStream(file1);
		int fileLength = (int) file1.length();
		return Tools.getBytesFromInputStream(fileinputstream, fileLength);
	}

	/**
	 * 写文件
	 *
	 * @param filename
	 *            文件名
	 * @param dataBytes
	 *            文件数据字节数组
	 * @throws IOException
	 */
	public static void writeFile(File file, byte[] dataBytes) throws IOException {
		if (dataBytes == null) {
			return;
		}
		Tools.writeFile(file, dataBytes, 0, dataBytes.length);
	}

	/**
	 * 写文件
	 *
	 * @param filename
	 *            文件名
	 * @param dataBytes
	 *            文件数据字节数组
	 * @param start
	 *            开始位置
	 * @param length
	 *            文件长度
	 * @throws IOException
	 */
	public static void writeFile(File file, byte[] dataBytes, int start, int length) throws IOException {
		if (dataBytes == null) {
			return;
		}
		String filename = file.getPath();
		// file.mkdirs();
		if (file.exists() && !file.canWrite()) {
			file.delete();
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filename);
			fileOut.write(dataBytes, start, length);
		} finally {
			try {
				fileOut.close();
			} catch (Throwable t) {
			}
		}
	}

	public static String[] getStreamStringArray(InputStream is, String charsetName, int lineCount) throws IOException {
		return Tools.getStreamStringArray(is, charsetName, false, lineCount);
	}

	public static String[] getStreamStringArray(InputStream is, String charsetName, boolean withNull, int lineCount) throws IOException {
		return Tools.getStreamStringList(is, charsetName, withNull, lineCount).toArray(new String[0]);
	}

	public static List<String> getStreamStringList(InputStream is, String charsetName, boolean withNull, int lineCount) throws IOException {
		Vector<String> vt = new Vector<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
		String tmp = br.readLine();
		int count = 1;
		while ((tmp != null) && (((lineCount > 0) && (count <= lineCount)) || (lineCount <= 0))) {
			if (!Tools.isEmpty(tmp) || withNull) {
				vt.addElement(tmp.trim());
			}
			tmp = br.readLine();
			count++;
		}
		br.close();
		is.close();
		return vt;
	}

	public static byte[] midBytes(byte[] src, int start, int length) {
		if (start < 0) {
			start = 0;
		}
		if (length <= 0) {
			return null;
		}
		if (start >= src.length) {
			return null;
		}
		if ((start + length) > src.length) {
			length = src.length - start;
		}
		byte[] dBytes = new byte[length];
		System.arraycopy(src, start, dBytes, 0, length);

		return dBytes;
	}

	public static String num2str(int i, int length) {
		String temp = Tools.getLengthChar(length, '0');
		String num = i + "";
		if (num.length() >= temp.length()) {
			return num;
		}
		return temp.substring(0, temp.length() - num.length()) + num;
	}

	static public String getLengthChar(int length, char spec) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(spec);
		}
		return buffer.toString();
	}

	public static String num2str(long i, int length) {
		String temp = Tools.getLengthChar(length, '0');
		String num = i + "";
		if (num.length() >= temp.length()) {
			return num;
		}
		return temp.substring(0, temp.length() - num.length()) + num;
	}

	/**
	 * 将字符整型转换成整形
	 *
	 * @param value
	 *            字符整型
	 * @return 整型
	 */
	public static Integer getIntValue(String value) {
		return Tools.getIntValue(value, 0);
	}

	public static Integer getIntValue(String value, int defaultValue) {
		try {
			value = value.concat(".");
			return Integer.parseInt(value.substring(0, value.indexOf(".")));
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	/**
	 * 将整型对象转换成整形
	 *
	 * @param value
	 *            整型对象
	 * @return 整型
	 */
	public static Integer getIntValue(Object value) {
		if (value == null) {
			return 0;
		}
		return Tools.getIntValue(value.toString());
	}

	public static Double getDoubleValue(String value) {
		if (Tools.isNumber(value)) {
			return Double.parseDouble(value);
		}
		return 0.0d;
	}

	public static boolean isNumber(String value) {
		if ((value == null) || value.equals("")) {
			return false;
		}
		if (value.indexOf(".") != value.lastIndexOf(".")) {
			return false;
		}
		int count = 0;
		for (int i = 0; i < value.length(); i++) {
			if (((value.charAt(i) >= '0') && (value.charAt(i) <= '9')) || (value.charAt(i) == '.')) {
				count++;
			}
		}
		if (count == value.length()) {
			return true;
		}
		return false;
	}

	public static long getLongValue(String value) {
		try {
			value = value.concat(".");
			return Long.parseLong(value.substring(0, value.indexOf(".")));
		} catch (Throwable e) {
			return 0;
		}
	}

	public static long getLongValue(Object value) {
		try {
			if (value == null) {
				return 0;
			}
			if (value instanceof Long) {
				return (Long) value;
			}
			String val = value.toString().concat(".");
			return Long.parseLong(val.substring(0, val.indexOf(".")));
		} catch (Throwable e) {
			return 0;
		}
	}

	/*
	 * public static String obj2json(Object obj) { try { if (obj == null) {
	 * return "{}"; } mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
	 * mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	 *
	 * return mapper.writeValueAsString(obj); } catch (Exception e) {
	 * System.out.println("==>> Object to JSON occer error: " + e); } return
	 * "{}"; }
	 */

	public static String getStringValue(Object val) {
		return Tools.getStringValue(val, "");
	}

	public static String getStringValue(Object val, String defaultValue) {
		if (val == null) {
			return defaultValue;
		}
		return val.toString();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection object) {
		return (null == object) || (object.size() <= 0);
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	public static Object json2Object(String json, Class clazz, boolean isCollection) {
		Object responseObject = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DeserializationConfig deserializationConfig = Tools.outMapper.getDeserializationConfig();
		deserializationConfig.setDateFormat(dateFormat);
		Tools.outMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		Tools.outMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		outMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		try {
			if (isCollection) {
				// 集合时调用
				TypeFactory t = TypeFactory.defaultInstance();
				responseObject = Tools.outMapper.readValue(json, t.constructCollectionType(List.class, clazz));
			} else {
				// 普通对象调用
				responseObject = Tools.outMapper.readValue(json, clazz);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return responseObject;
	}

	public static String obj2json(Object obj) {
		try {
			if (obj == null) {
				return "{}";
			}
			// mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			Tools.mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

			return Tools.mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("==>> Object to JSON occer error: ", e);
		}
		return "{}";
	}

	public static String getToday() {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(1);
		int j = calendar.get(2) + 1;
		int k = calendar.get(5);
		return i + "-" + (j >= 10 ? j + "" : "0" + j) + "-" + (k >= 10 ? k + "" : "0" + k);
	}

	/**
	 * 获取当天8位数字型日期
	 *
	 * @return String
	 */
	public static String getTodayString() {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(1);
		int j = calendar.get(2) + 1;
		int k = calendar.get(5);
		return i + (j >= 10 ? j + "" : "0" + j) + (k >= 10 ? k + "" : "0" + k);
	}

	/**
	 *
	 * @return String
	 */
	public static List<Long> StrListToLongList(List<String> strList) {
		List<Long> result = new ArrayList<Long>();
		for (String str : strList) {
			result.add(Long.valueOf(str));
		}
		return result;
	}

	/**
	 *
	 * @return ListInteger
	 */
	public static List<Integer> StrListToIntegerList(List<String> strList) {
		List<Integer> result = new ArrayList<Integer>();
		for (String str : strList) {
			result.add(Integer.valueOf(str));
		}
		return result;
	}

	public static String splitReverse(List<Object> objects, String symbol) {
		StringBuilder res = new StringBuilder();
		int i = 0;
		for (Object o : objects) {
			res.append(String.valueOf(o));
			if ((i != (objects.size() - 1))) {
				res.append(symbol);
			}
			i++;
		}
		return res.toString();
	}

	/**
	 *
	 * 获取当前时间开始的字符串 如2014-10-28 00:00:00
	 *
	 * @param
	 * @return
	 */
	public static String getTodayBeginTime(String patten) {
		if (patten == null) {
			patten = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		String formatStr = sdf.format(new java.util.Date());
		String timeStr = formatStr.substring(0, formatStr.length() - 8);
		return timeStr + "00:00:00";
	}

	public static boolean isEmptyZero(Long obj) {
		if ((obj != null) && (obj > 0)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isEmptyZero(Integer obj) {
		if ((obj != null) && (obj > 0)) {
			return false;
		} else {
			return true;
		}
	}

	public static String nullConvertToEmptyString(String string) {
		String lastusername = string == null ? "" : string;
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

	public static Long nullConvertToLong(String bd) {
		Long lastusername = bd == null ? null : Long.parseLong(bd);
		return lastusername;
	}

	public static Integer nullConvertToInteger(String bd) {
		Integer lastusername = bd == null ? null : Integer.parseInt(bd);
		return lastusername;
	}

	public static String format(String smsTemplate, Map<String, String> param) {
		Matcher m = Tools.pIndex.matcher(smsTemplate);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		while (result) {
			if (param.get(m.group(1)) != null) {
				m.appendReplacement(sb, param.get(m.group(1)));
			}
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static String join(Collection<String> list, char sep) {
		String result = "";
		if (list != null) {
			for (String str : list) {
				result += str + sep;
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	public static Map<String, Integer> parseKvMap(String kvStr) {
		String s[] = kvStr.split(";");
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (s.length > 0) {
			for (int i = 0; i < s.length; i++) {
				String s1[] = s[i].split(":");
				map.put(s1[0], Integer.parseInt(s1[1]));
			}
		}
		return map;
	}

	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	/**
	 * 获取本周周一时间
	 *
	 * @return
	 */
	public static String getMonday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		return Tools.sdf.format(calendar.getTime());
	}

	public static String getIp() {
		InetAddress addr = null;
		String ip = "";
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();// 获得本机IP
		} catch (Exception e) {
			logger.error("", e);
		}
		return ip;
	}

	public static void main(String paras[]) {
		String template = "您好，您在[供应商]的订单已由[小件员][小件员电话]送出，代收款[代收款金额]，18点前查询及投诉请拨打010-85896659【易普联科】";
		Map<String, String> param = new HashMap<String, String>();
		param.put("供应商", "唯品会");
		param.put("小件员电话", "15601971499");
		logger.info(Tools.format(template, param));
	}

	public static String join(List<String> list, String sep) {
		String result = "";
		if (list != null) {
			for (String str : list) {
				result += str + sep;
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 获取Excel表格单元格值
	 *
	 * @param excelExtractor
	 *            Excel解释器
	 * @param row
	 *            当前行
	 * @param colIndex
	 *            列序号
	 * @return
	 */
	public static String getCellData(ExcelExtractor excelExtractor, Object row, int colIndex) {
		try {
			return excelExtractor.getXRowCellData(row, colIndex);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Map 转为JavaBean对象
	 *
	 * @param type
	 *            JavaBean对象
	 * @param map
	 *            Map
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map<String, Object> map) {
		String[] names = map.keySet().toArray(new String[0]);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			StringBuilder tname = new StringBuilder();
			if (name.indexOf("_") > 0) {
				int start = 0;
				int end = name.indexOf("_");
				while (end > 0) {
					tname.append(name.substring(start, end));
					start = end + 1;
					tname.append((char) (name.charAt(start) - 32));
					start++;
					end = name.indexOf("_", start);
				}
				if (start < name.length()) {
					tname.append(name.substring(start, name.length()));
				}
				map.put(tname.toString(), map.get(name));
				map.remove(name);
			}
		}
		String json = Tools.obj2json(map);
		return Tools.json2Object(json, type, false);
		/*
		 * 下面方法经常抛异常 BeanInfo beanInfo = Introspector.getBeanInfo(type); //
		 * 获取类属性 Object obj = type.newInstance(); // 创建 JavaBean 对象
		 * 
		 * // 给 JavaBean 对象的属性赋值 PropertyDescriptor[] propertyDescriptors =
		 * beanInfo .getPropertyDescriptors(); for (int i = 0; i <
		 * propertyDescriptors.length; i++) { PropertyDescriptor descriptor =
		 * propertyDescriptors[i]; String propertyName = descriptor.getName();
		 * StringBuilder sb = new StringBuilder(); for (int j = 0; j <
		 * propertyName.length(); j++) { if (propertyName.charAt(j) >= 'A' &&
		 * propertyName.charAt(j) <= 'Z') {
		 * sb.append("_"+(char)(propertyName.charAt(j)+32)); } else {
		 * sb.append(propertyName.charAt(j)); } }
		 * 
		 * if (map.containsKey(sb.toString())) { // 下面一句可以 try
		 * 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。 Object value = map.get(sb.toString());
		 * 
		 * Object[] args = new Object[1]; args[0] = value;
		 * 
		 * try { descriptor.getWriteMethod().invoke(obj, args); } catch
		 * (Throwable t) { t.printStackTrace(); } } } return obj;
		 */
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 *
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				String classType = readMethod.getGenericReturnType().toString().substring(6);
				Object result = readMethod.invoke(bean, new Object[0]);
				if ((result == null) && classType.equals("java.lang.Integer")) {
					result = 0;
				} else if ((result == null) && classType.equals("java.lang.Long")) {
					result = 0l;
				} else if ((result == null) && classType.equals("java.math.BigDecimal")) {
					result = BigDecimal.ZERO;
				} else if ((result == null) && classType.equals("java.lang.String")) {
					result = "";
				}
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < propertyName.length(); j++) {
					if ((propertyName.charAt(j) >= 'A') && (propertyName.charAt(j) <= 'Z')) {
						sb.append("_" + (char) (propertyName.charAt(j) + 32));
					} else {
						sb.append(propertyName.charAt(j));
					}
				}
				// if (result != null) {
				returnMap.put(sb.toString(), result);
				/*
				 * } else { returnMap.put(sb.toString(), ""); }
				 */
			}
		}
		return returnMap;
	}

	public static String getDateFromToday(int avail) {
		long lg = System.currentTimeMillis();
		return Tools.getDateString(lg + (avail * 86400000L));
	}

	public static String getDateString(long mill) {
		java.sql.Date date = new java.sql.Date(mill);
		return date.toString();
	}

	public static void outData2Page(String s, HttpServletResponse response) throws IOException {
		Tools.outData2Page(s, response, "text/html; charset=UTF-8");
	}

	public static void outData2Page(String s, HttpServletResponse response, String contentType) throws IOException {
		response.setContentType(contentType);
		PrintWriter printwriter = response.getWriter();
		printwriter.print(s);
		printwriter.close();
	}

	public static String getLastMonthFirstday() {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(Calendar.YEAR);
		int j = calendar.get(Calendar.MONTH);
		if (j == 0) {
			return (i - 1) + "-12-01";
		}
		return i + "-" + (j >= 10 ? j + "" : "0" + j) + "-01";
	}

	public static String getThisMonthFirstday() {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(Calendar.YEAR);
		int j = calendar.get(Calendar.MONTH);
		return i + "-" + (j >= 9 ? (j + 1) + "" : "0" + (j + 1)) + "-01";
	}

	/**
	 * 将字符串日期换成java.sql.Date
	 *
	 * @param sDate
	 *            有效时间字符
	 * @return 返回日期
	 */
	public static java.sql.Date str2date(String sDate) {
		if ((sDate == null) || sDate.equals("")) {
			return null;
		}
		if ((sDate.charAt(0) > '9') || (sDate.charAt(0) < '0')) {
			return null;
		}
		sDate = sDate.replace('/', '-');
		String[] ar = Tools.strSplit(sDate, "- :");
		if (ar.length < 3) {
			return null;
		}
		return java.sql.Date.valueOf(ar[0] + "-" + ar[1] + "-" + ar[2]);
	}

	public static String getNextDay(String sdate) {
		java.sql.Date d = Tools.str2date(sdate);
		if (d != null) {
			java.sql.Date d1 = new java.sql.Date(d.getTime() + (24 * 3600 * 1000L));
			return d1.toString();
		}
		return null;
	}

	public static String list2Str(List<?> list) {
		String str = "";
		if ((null != list) && (list.size() > 0)) {
			for (Object obj : list) {
				str += obj.toString() + ",";
			}
		}
		return str.substring(0, str.length() - 1);
	}

	/*
	 * 字符串加上单引号
	 */
	public static String spilt(String str) {
		StringBuffer sb = new StringBuffer();
		String[] temp = str.split(",");
		for (int i = 0; i < temp.length; i++) {
			if (!"".equals(temp[i]) && (temp[i] != null)) {
				sb.append("'" + temp[i] + "',");
			}
		}
		String result = sb.toString();
		String tp = result.substring(result.length() - 1, result.length());
		if (",".equals(tp)) {
			return result.substring(0, result.length() - 1);
		} else {
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> String assembleInByList(List<T> list) {
		if ((list == null) || list.isEmpty()) {
			return "";
		}
		StringBuffer sql = new StringBuffer(" in (");
		StringBuffer inItemSql = new StringBuffer();
		Class<? extends Object> dataClass = list.get(0).getClass();
		if (dataClass.equals(Integer.class)) {
			for (Integer integer : (List<Integer>) list) {
				inItemSql.append(integer);
				inItemSql.append(",");
			}
		} else if (dataClass.equals(String.class)) {
			for (String str : (List<String>) list) {
				inItemSql.append("'");
				inItemSql.append(str);
				inItemSql.append("'");
				inItemSql.append(",");
			}
		} else if (dataClass.equals(Long.class)) {
			for (Long l : (List<Long>) list) {
				inItemSql.append(l);
				inItemSql.append(",");
			}
		}
		String inItemSqlStr = inItemSql.toString();
		sql.append(inItemSqlStr.substring(0, inItemSqlStr.length() - 1));// 去掉最后一个逗号
		sql.append(")");
		return sql.toString();
	}

	/**
	 * added by jiangyu 拷贝值 将空值赋值为默认值
	 *
	 * @param dest
	 * @param origon
	 * @return
	 * @throws Exception
	 */
	public static Object copyBeanValueNull2Default(Object dest, Object origon) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Class orgionClazz = origon.getClass();
		Class destClazz = dest.getClass();
		Field[] fields = orgionClazz.getDeclaredFields();
		for (Field field : fields) {
			String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + "" + field.getName().substring(1);
			Method getMethod = null;
			try{
				getMethod = orgionClazz.getDeclaredMethod(getMethodName, null);
			}catch(NoSuchMethodException e){
				logger.error("属性：" + field.getName() + "的get方法不存在");
			}
			if(null != getMethod){
				getMethod.setAccessible(true);
				Object value = getMethod.invoke(origon, null);
				if (value != null) {
					map.put(field.getName(), String.valueOf(value));
				} else {
					map.put(field.getName(), "");
				}
			}
		}

		Field[] destFields = destClazz.getDeclaredFields();
		for (Field field : destFields) {
			String filedClassType = field.getType().getName();
			String setMethodName = "set" + field.getName().substring(0, 1).toUpperCase() + "" + field.getName().substring(1);
			Method setMethod = destClazz.getDeclaredMethod(setMethodName, field.getType());
			String value = map.get(field.getName());
			Object targetVal = null;
			if ("java.lang.Integer".equals(filedClassType)) {
				if (value != null) {
					targetVal = Integer.valueOf(value);
				} else {
					targetVal = 0;
				}
			} else if ("java.lang.Long".equals(filedClassType)) {
				if (value != null) {
					targetVal = Long.valueOf(value);
				} else {
					targetVal = 0L;
				}
			} else if ("java.math.BigDecimal".equals(filedClassType) || "java.math.Double".equals(filedClassType)) {
				if (value != null) {
					targetVal = new BigDecimal(value);
				} else {
					targetVal = BigDecimal.ZERO;
				}
			} else if ("java.lang.String".equals(filedClassType)) {
				if (value != null) {
					targetVal = value;
				} else {
					targetVal = "";
				}
			}
			setMethod.setAccessible(true);
			setMethod.invoke(dest, new Object[] { targetVal });
		}
		return dest;
	}
	
	//将list转换为xml格式的String类型（List中存放的对象为实体类）* 
	public static <T> String getObjectToXml(List<T> list) { 
		StringBuffer stringBuffer = new StringBuffer(); 
		// 循环遍历list 
		for (int i = 0; i < list.size(); i++) { 
			Object object = list.get(i); 
			// 取得实体类中的每个元素
			Field[] fields = object.getClass().getDeclaredFields(); 
			// 遍历所有元素
			for (int j = 0; j < fields.length; j++) { 
				try { 
					String name = fields[j].getName(); 
					// 拼接get方法名
					Method method = object.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class[] {});
					// 利用反射机制，取得对应的数据
					Object result = method.invoke(object, new Object[] {}); 
					stringBuffer.append("<" + name); 
					stringBuffer.append(">"); 
					stringBuffer.append(result); 
					stringBuffer.append("</" + name); 
					stringBuffer.append(">"); 
				} catch (Exception e) { 
					e.getStackTrace(); 
				} 
			} 
		} 
		return stringBuffer.toString(); 
	}
}
