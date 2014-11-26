package cn.explink.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDayUtil {
	private static long getQuot(String endTime, String strateTime) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(endTime);
			Date date2 = ft.parse(strateTime);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	public static long getQuotHore(String endTime, String strateTime) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = ft.parse(endTime);
			Date date2 = ft.parse(strateTime);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	public static String getDayBefore(int dayCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -dayCount); // 得到前一天
		int dayFor = calendar.get(Calendar.DATE);// 前7天
		int monthFor = calendar.get(Calendar.MONTH);
		int yesr = calendar.get(Calendar.YEAR);// 当前年
		return "" + yesr + "-" + monthFor + "-" + dayFor;
	}

	public static String getDayCum(String nowday, int dayCount) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long dayCountLong = Long.parseLong(dayCount + "");
			Date d = new Date(f.parse(nowday).getTime() + dayCountLong * 24 * 3600 * 1000);
			return f.format(d);
		} catch (Exception ex) {
			return "输入格式错误";
		}
	}

	public static String getDayCumLong(String nowday, int dayCount) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = new Date(f.parse(nowday).getTime() + dayCount * 24 * 3600 * 1000);
			return f.format(d);
		} catch (Exception ex) {
			return "输入格式错误";
		}
	}

	public static long getDaycha(String stratedate, String enddate) {
		long dayCha = -1;
		if (stratedate.length() > 0 && enddate.length() > 0) {
			dayCha = DateDayUtil.getQuot(enddate, stratedate);
		} else if (stratedate.length() < 0 && enddate.length() > 0) {
			stratedate = enddate;
			dayCha = DateDayUtil.getQuot(enddate, stratedate);
		} else if (stratedate.length() > 0 && enddate.length() < 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date());
			dayCha = DateDayUtil.getQuot(enddate, stratedate);
		}
		return dayCha;
	}

	public static String getDateBeforedays(String date, long dayCount) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = new Date(f.parse(date).getTime() - dayCount * 24 * 3600 * 1000);
			return f.format(d);
		} catch (Exception ex) {
			return "输入格式错误";
		}
	}

	// 获取几天前的时间
	public static String getDateBefore(String date, int dayCount) {
		if (date.equals("")) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return DateDayUtil.getDayCum(sdf.format(new Date()), dayCount);
		} else {
			return DateDayUtil.getDayCum(date, dayCount);
		}

	}

	public static String getDateAfter(String date, int dayCount) {
		if (!date.equals("")) {
			return DateDayUtil.getDayCumLong(date, dayCount);
		}
		return "";
	}

	public static Date getTimeOf12() {
		return getTimeOf12(0);
	}

	public static Date getTimeOf12(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 0);
		cal.roll(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date getTimeByDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
}
