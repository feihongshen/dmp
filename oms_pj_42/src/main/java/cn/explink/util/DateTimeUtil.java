package cn.explink.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * Utility to date time
 * 
 * @version 0.1
 * @author Super Zhao
 */
public class DateTimeUtil {

	/**
	 * 由java.util.Date到java.sql.Date的类型转换
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getSqlDate(java.util.Date date) {
		return new Date(date.getTime());
	}

	public static Date nowDate() {
		Calendar calendar = Calendar.getInstance();
		return getSqlDate(calendar.getTime());
	}

	/**
	 * 转换时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(java.util.Date date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getNowTime();
		}
	}

	/**
	 * 转换时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(java.util.Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getNowTime();
		}
	}

	/**
	 * 转换时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatStrToDate(String time) {
		try {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date d = sim.parse(time);
			return (Date) d;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 转换时间格式 yyyyMMddHHmmss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateLong(java.util.Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getNowTime();
		}
	}

	// 获取当前系统日期时间 yyyy-MM-dd HH:mm:ss
	public static String getNowTime() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d = new java.util.Date();
		return df.format(d);

	}

	// 获取当前日期某一天前的数据
	public static String getDateBefore(int day) {
		long currentMillis = System.currentTimeMillis();
		long beforeSS = currentMillis / 1000 - (24 * 60 * 60 * day);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beforeSS * 1000));
	}

	// 获取当前日期某小时 前的数据
	public static String getDateBeforeHours(int hours, Date date) {
		long currentMillis = date.getTime();

		long beforeSS = currentMillis / 1000 - (60 * 60 * hours);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beforeSS * 1000));
	}

	// 获取时间加5分钟
	public static String getDate5min(Timestamp date, int min) {
		long currentMillis = date.getTime();
		long beforeSS = currentMillis / 1000 + (60 * min);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(beforeSS * 1000));
	}

	// 获取当前系统日期时间 到分钟 yyyy-MM-dd HH:mm
	public static String getNowTimeMin() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date d = new java.util.Date();
		return df.format(d);
	}

	// 获取当前系统日期
	public static String getNowDate() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		return df.format(d);
	}

	// 获取当前时间指定小时后的时间
	/**
	 *
	 * @return String
	 */
	public static String getNeedHourTime(int count) {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date d = new java.util.Date();

		String time = df.format(d);
		String hour = time.substring(0, 2);
		int newhour = (Integer.parseInt(hour) + count) >= 24 ? ((Integer.parseInt(hour) + count) - 24) : (Integer.parseInt(hour) + count);
		return newhour < 10 ? "0" + newhour + ":00" : newhour + ":00";
	}

	/**
	 * 获得某一日期的后一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getNextDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + 1);
		return getSqlDate(calendar.getTime());
	}

	/**
	 * 获得某一日期的后一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static java.util.Date nextDate(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static java.util.Date previousDate(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获得某一日期的前一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		return getSqlDate(calendar.getTime());
	}

	/**
	 * 获得某年某月第一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return Date
	 */
	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		return getSqlDate(calendar.getTime());
	}

	/**
	 * 获得某年某月最后一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return Date
	 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		return getPreviousDate(getSqlDate(calendar.getTime()));
	}

	/**
	 * 获取相差指定秒钟后的时间
	 * 
	 * @param min
	 *            int
	 * @return String
	 */
	public static String getNeedSecondDate(int second) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now2 = new Date(System.currentTimeMillis() + 1000 * second);
		String time2 = formatter.format(now2);
		return time2;
	}

	/**
	 * 获取指定日期相差指定天数的日期
	 * 
	 * @param strDate
	 *            格式 yyyy-MM-dd
	 * @param nCount
	 *            相差天数 可以有正负
	 * @return
	 */
	public static String getNeedDate(String strDate, int nCount) {
		GregorianCalendar worldTour = new GregorianCalendar(Integer.parseInt(strDate.substring(0, 4)), Integer.parseInt(strDate.substring(5, 7)) - 1, Integer.parseInt(strDate.substring(8)));
		worldTour.add(GregorianCalendar.DATE, nCount);
		java.util.Date d = worldTour.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}

	/**
	 * 获取指定日期相差指定小时数的日期
	 * 
	 * @param strDate
	 *            格式 yyyy-MM-dd
	 * @param hours
	 *            相差天数 可以有正负
	 * @return 2013-11-05 18:20:20
	 */
	public static String getNeedHours(String strDate, int hours) {
		GregorianCalendar worldTour = new GregorianCalendar(Integer.parseInt(strDate.substring(0, 4)), Integer.parseInt(strDate.substring(5, 7)), Integer.parseInt(strDate.substring(8, 10)),
				Integer.parseInt(strDate.substring(11, 13)), Integer.parseInt(strDate.substring(14, 16)), Integer.parseInt(strDate.substring(17, 19)));
		worldTour.add(GregorianCalendar.HOUR, -hours);

		java.util.Date d = worldTour.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	/**
	 * 由年月日构建java.sql.Date类型
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return Date
	 */
	public static Date buildDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date);
		return getSqlDate(calendar.getTime());
	}

	/**
	 * 取得某月的天数
	 * 
	 * @param year
	 * @param month
	 * @return int
	 */
	public static int getDayCountOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 0);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获得某年某季度的最后一天的日期
	 * 
	 * @param year
	 * @param quarter
	 * @return Date
	 */
	public static Date getLastDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = quarter * 3;
		}
		return getLastDayOfMonth(year, month);

	}

	/**
	 * 获得某年某季度的第一天的日期
	 * 
	 * @param year
	 * @param quarter
	 * @return Date
	 */
	public static Date getFirstDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = (quarter - 1) * 3 + 1;
		}
		return getFirstDayOfMonth(year, month);
	}

	/**
	 * 获得某年的第一天的日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static Date getFirstDayOfYear(int year) {
		return getFirstDayOfMonth(year, 1);
	}

	/**
	 * 获得某年的最后一天的日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static Date getLastDayOfYear(int year) {
		return getLastDayOfMonth(year, 12);
	}

	/**
	 * String到java.sql.Date的类型转换
	 * 
	 * @param param
	 * @return Date
	 */
	public static java.sql.Date StringToDate(String param) {

		java.util.Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(param);
			return new Date(date.getTime());
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算两个时间相差的小时数
	 * 
	 * @return long
	 */
	public static double getHourFromToTime(String time1, String time2) {
		double hour = 0;
		if (!time1.equals("") && time1.length() == 19 && !time2.equals("") && time2.length() == 19) {
			GregorianCalendar gc1 = new GregorianCalendar(Integer.parseInt(time1.substring(0, 4)), Integer.parseInt(time1.substring(5, 7)) - 1, Integer.parseInt(time1.substring(8, 10)),
					Integer.parseInt(time1.substring(11, 13)), Integer.parseInt(time1.substring(14, 16)), Integer.parseInt(time1.substring(17, 19)));

			GregorianCalendar gc2 = new GregorianCalendar(Integer.parseInt(time2.substring(0, 4)), Integer.parseInt(time2.substring(5, 7)) - 1, Integer.parseInt(time2.substring(8, 10)),
					Integer.parseInt(time2.substring(11, 13)), Integer.parseInt(time2.substring(14, 16)), Integer.parseInt(time2.substring(17, 19)));

			// the above two dates are one second apart
			java.util.Date d1 = gc1.getTime();
			java.util.Date d2 = gc2.getTime();
			long l1 = d1.getTime();
			long l2 = d2.getTime();
			long difference = l2 - l1; // 毫秒数
			hour = difference / 1000 / 60 / 60; // 小时数
			// 四舍五入取整
			java.math.BigDecimal b = new java.math.BigDecimal(hour);
			hour = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return hour;

	}

	// 两个时间差的天数
	public static double getDaysFromToTime(String time1, String time2) {
		double hour = getHourFromToTime(time1, time2);
		return hour / 24 < 0.01 ? 1 : hour / 24;
	}

	/**
	 * 计算两个时间相差的小时数
	 * 
	 * @return string
	 */
	public static String getHourFromToTimeforString(String time1, String time2) {
		double hour = 0;
		if (!time1.equals("") && time1.length() == 19 && !time2.equals("") && time2.length() == 19) {
			GregorianCalendar gc1 = new GregorianCalendar(Integer.parseInt(time1.substring(0, 4)), Integer.parseInt(time1.substring(5, 7)) - 1, Integer.parseInt(time1.substring(8, 10)),
					Integer.parseInt(time1.substring(11, 13)), Integer.parseInt(time1.substring(14, 16)), Integer.parseInt(time1.substring(17, 19)));

			GregorianCalendar gc2 = new GregorianCalendar(Integer.parseInt(time2.substring(0, 4)), Integer.parseInt(time2.substring(5, 7)) - 1, Integer.parseInt(time2.substring(8, 10)),
					Integer.parseInt(time2.substring(11, 13)), Integer.parseInt(time2.substring(14, 16)), Integer.parseInt(time2.substring(17, 19)));

			// the above two dates are one second apart
			java.util.Date d1 = gc1.getTime();
			java.util.Date d2 = gc2.getTime();
			long l1 = d1.getTime();
			long l2 = d2.getTime();
			long difference = l2 - l1; // 毫秒数
			hour = difference / 1000 / 60 / 60; // 小时数
			// 四舍五入取整
			java.math.BigDecimal b = new java.math.BigDecimal(hour);
			hour = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return hour == 0 ? "" : hour + "";
	}

	// 获得有效日期
	public static String getValidDate(String date, String year, String month, String day) {
		if (date.equals("2004-02-30") || date.equals("2004-02-31") || date.equals("2008-02-30") || date.equals("2008-02-31")) {
			day = "29";
		} else if ((month.equals("04") || month.equals("06") || month.equals("09") || month.equals("11")) && day.equals("31")) {
			day = "30";
		} else if (month.equals("02") && ((day.equals("29") || day.equals("30") || day.equals("31")))) {
			if (runNian(Integer.parseInt(year))) {// 闰年
				day = "29";
			} else {// 非闰年
				day = "28";
			}
		}
		return year + "-" + month + "-" + day;

	}

	static boolean runNian(int year) { // 判断是否为闰年的方法
		boolean t = false;
		if (year % 4 == 0) {
			if (year % 100 != 0) {
				t = true;
			} else if (year % 400 == 0) {
				t = true;
			}
		}
		return t;
	}

	// 获取两个日期间的日期
	public static List findDatesfromDates(String dBegin_s, String dEnd_s) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date dBegin = sdf.parse(dBegin_s);
		java.util.Date dEnd = sdf.parse(dEnd_s);

		List datelist = new ArrayList();

		datelist.add(sdf.format(dBegin));

		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(dBegin);

		boolean bContinue = true;

		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (dEnd.after(cal.getTime())) {
				datelist.add(sdf.format(cal.getTime()));
			} else {
				break;
			}
		}
		datelist.add(sdf.format(dEnd));
		return datelist;
	}

	// 获取两个日期间的月份
	public static List findMonthsfromDates(String dBegin_s, String dEnd_s) {
		String startyear = dBegin_s.substring(0, 4);
		String endyear = dEnd_s.substring(0, 4);

		String startmonth = dBegin_s.substring(5, 7);
		String endmonth = dEnd_s.substring(5, 7);
		List monthlist = new ArrayList();
		for (int i = Integer.parseInt(startyear); i <= Integer.parseInt(endyear); i++) { // 取出每月的数据
			if (i == Integer.parseInt(endyear)) { // 年份一样,月份不一样
				for (int m = Integer.parseInt(startmonth); m <= Integer.parseInt(endmonth); m++) {

					int len = ("0" + m).length();
					String date = "" + i + "-" + ("0" + m).substring(len - 2, len); // 格式yyyy-mm
					monthlist.add(date);
				}
				break;
			} else { // 年份,月份都不一样
				for (int m = Integer.parseInt(startmonth); m <= 12; m++) { // 先循环到12月
					int len = ("0" + m).length();
					String date = "" + i + "-" + ("0" + m).substring(len - 2, len); // 格式yyyy-mm
					monthlist.add(date);
				}
				startmonth = "01";
			}
		}

		return monthlist;

	}

	// 获取两个日期间的年份
	public static List findYearsfromDates(String dBegin_s, String dEnd_s) {
		String startyear = dBegin_s.substring(0, 4);
		String endyear = dEnd_s.substring(0, 4);

		List yearlist = new ArrayList();
		for (int i = Integer.parseInt(startyear); i <= Integer.parseInt(endyear); i++) {
			yearlist.add("" + i);
		}
		return yearlist;

	}

	// 当前日期的前90天内数据
	public static String getIndexStartDayZeroTime() {
		String currentdate = getNowDate();
		String last20date = getNeedDate(currentdate, -90);
		System.out.println("====时间索引起始时间:====" + last20date + " 00:00:00");
		return last20date + " 00:00:00";
	}

	// 当前日期的时间 20091221 改成了90天内的数据
	public static String getCurrentDayZeroTime() {
		String currentdate = getNowDate();
		String last20date = getNeedDate(currentdate, -90);
		System.out.println("====时间索引起始时间:====" + last20date + " 00:00:00");
		return last20date + " 00:00:00";
		// return currentdate + " 00:00:00";
	}

	/**
	 * 输出自定义格式的日期格式
	 * 
	 * @param formatstring
	 * @return
	 */
	public static String getNowTime(String formatstring) {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(formatstring);
		java.util.Date d = new java.util.Date();
		return df.format(d);
	}

	// 获取当前日期的前一个月 2010-01 格式
	public static String getLastYearMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		calendar.add(Calendar.MONTH, -1); // 得到前一个月
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; // 注意月份要加一
		return year + "-" + month;
	}

	// 获取指定时间多少秒钟后的时间 yyyy-mm-dd HH:mm:ss 秒钟可以为正负数
	public static String getNeedSecondDate_ByDate(String strDateTime, int second) {
		GregorianCalendar worldTour = new GregorianCalendar(Integer.parseInt(strDateTime.substring(0, 4)), Integer.parseInt(strDateTime.substring(5, 7)) - 1, Integer.parseInt(strDateTime.substring(8,
				10)), Integer.parseInt(strDateTime.substring(11, 13)), Integer.parseInt(strDateTime.substring(14, 16)), Integer.parseInt(strDateTime.substring(17, 19)));
		worldTour.add(GregorianCalendar.SECOND, second);
		java.util.Date d = worldTour.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	// 获取文件最后修改时间
	public static String getFileLastUpdateTime(String filepathandname) {
		File file = new File(filepathandname);
		Long time = file.lastModified();
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(time);
		java.util.Date d = cd.getTime();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	/**
	 * 获取文件的创建时间
	 * 
	 * @param filepath
	 * @return
	 */
	public List<File> getFileList(String filepath) {

		try {
			File file = new File(filepath);
			if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File Ifile = new File(filepath + "\\" + filelist[i]);
					FileInputStream fis = new FileInputStream(Ifile);

					long modifiedTime = Ifile.lastModified();
					Date date = new Date(modifiedTime);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
					String dd = sdf.format(date);
					System.out.println("File name:" + Ifile.getName() + " \tFile size: " + (double) ((double) fis.available() / 1024 / 1024) + "M" + " \tFile create Time: " + dd);

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 读取文件创建时间
	 */
	public static String getCreateTime(HttpServletRequest request) {
		String filePath = request.getRealPath("uploaddata\\");

		String strTime = null;
		try {
			Process p = Runtime.getRuntime().exec("cmd /C dir " + filePath + "/tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith(".xls")) {
					strTime = line.substring(0, 17);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("创建时间    " + strTime);
		return strTime;
		// 输出：创建时间 2009-08-17 10:21
	}

	/**
	 * 读取文件修改时间的方法1
	 */
	@SuppressWarnings("deprecation")
	public static void getModifiedTime_1() {
		File f = new File("D:\\20120703103633[淘宝网]--邮编地址数据列表.xls");
		Calendar cal = Calendar.getInstance();
		long time = f.lastModified();
		cal.setTimeInMillis(time);
		// 此处toLocalString()方法是不推荐的，但是仍可输出
		System.out.println("修改时间[1] " + cal.getTime().toLocaleString());
		// 输出：修改时间[1] 2009-8-17 10:32:38
	}

	/**
	 * 读取修改时间的方法2
	 */
	public static void getModifiedTime_2() {
		File f = new File("C:\\test.txt");
		Calendar cal = Calendar.getInstance();
		long time = f.lastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal.setTimeInMillis(time);
		System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
		// 输出：修改时间[2] 2009-08-17 10:32:38
	}

	/**
	 * 获取当前时间点 小时
	 * 
	 * @return
	 */
	public static long getNowHours() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("HH");
		java.util.Date d = new java.util.Date();
		return Long.valueOf(df.format(d));
	}

	/**
	 * 判断两个日期大小，如日期1小于日期2，返回true，否则返回false
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isBefore(java.util.Date one, java.util.Date two) {
		if (one == null || two == null) {
			return false;
		}
		return one.getTime() < two.getTime();
	}

	/**
	 * 判断两个日期大小，如日期1大于日期2，返回true，否则返回false
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isAfter(java.util.Date one, java.util.Date two) {
		if (one == null || two == null) {
			return false;
		}
		return one.getTime() > two.getTime();
	}

	public static java.util.Date parse(String dateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateString);
	}

	public static java.util.Date after(java.util.Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

}
