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

	// 获取距离当前时间分钟数
	public static long getQuotMin(String strateTime) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = new Date();
			Date date2 = ft.parse(strateTime);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60;
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

	// 获取几天前的时间
	public static String getDateBefore(String date, int dayCount) {
		if (date.equals("")) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return DateDayUtil.getDayCum(sdf.format(new Date()), dayCount);
		} else {
			return DateDayUtil.getDayCum(date, dayCount);
		}

	}
}
