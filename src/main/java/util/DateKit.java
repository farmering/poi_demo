
package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateKit {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final static SimpleDateFormat sdfmsTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private final static SimpleDateFormat allTime = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 获取YYYY格式
	 * 
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY格式
	 */
	public static String getYear(Date date) {
		return sdfYear.format(date);
	}

	/**
	 * 获取YYYY-MM-DD格式
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 */
	public static String getDay(Date date) {
		return sdfDay.format(date);
	}

	/**
	 * 获取YYYYMMDD格式
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 */
	public static String getDays(Date date) {
		return sdfDays.format(date);
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}
	
	/**
	 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
	 */
	public static String getMsTime() {
		return sdfmsTime.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDDHHmmss格式
	 */
	public static String getAllTime() {
		return allTime.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 */
	public static String getTime(Date date) {
		return sdfTime.format(date);
	}

	/**
	 * @Title: 字符串日期比较
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 */
	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}

	/**
	 * 格式化日期
	 */
	public static Date parseDate(String date) {
		try {
			return sdfDay.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 */
	public static Date parseTime(String date) {
		try {
			return sdfTime.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 格式化日期
	 */
	public static Date parse(String date, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 */
	public static String format(Date date, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		return fmt.format(date);
	}

	/**
	 * 把日期转换为Timestamp
	 */
	public static Timestamp format(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 校验日期是否合法
	 */
	public static boolean isValidDate(String s) {
		try {
			sdfTime.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	/**
	 * 校验日期是否合法
	 */
	public static boolean isValidDate(String s, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 得到n天之后的日期
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}
	/**
	 * 取得Integer型的当前日期
	 * 
	 * @return
	 */
	public static Integer getIntNow() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前日期
	 * 
	 * @return
	 */
	public static Integer getIntToday() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前年份
	 * 
	 * @return
	 */
	public static Integer getIntYearNow() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得Integer型的当前月份
	 * 
	 * @return
	 */
	public static Integer getIntMonthNow() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		return month;
	}

	public static String getStringToday() {
		return getIntDate(getNow()) + "";
	}

	/**
	 * 根据年月日获取整型日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Integer getIntDate(int year, int month, int day) {
		return getIntDate(newCalendar(year, month, day));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getFirstDayOfMonth(int year, int month) {
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getFirstDayOfThisMonth() {
		Integer year = DateUtil.getIntYearNow();
		Integer month = DateUtil.getIntMonthNow();
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param date
	 * @return
	 * @time:2008-7-4 上午09:58:55
	 */
	public static Integer getFistDayOfMonth(Date date) {
		Integer intDate = getIntDate(date);
		int year = intDate / 10000;
		int month = intDate % 10000 / 100;
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getLastDayOfMonth(int year, int month) {
		return intDateSub(getIntDate(newCalendar(year, month + 1, 1)), 1);
	}

	/**
	 * 根据Calendar获取整型年份
	 * 
	 * @param c
	 * @return
	 */
	public static Integer getIntYear(Calendar c) {
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 根据Calendar获取整型日期
	 * 
	 * @param c
	 * @return
	 */
	public static Integer getIntDate(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	/**
	 * 根据Date获取整型年份
	 * 
	 * @param d
	 * @return
	 */
	public static Integer getIntYear(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntYear(c);
	}

	/**
	 * 根据Date获取整型日期
	 * 
	 * @param d
	 * @return
	 */
	public static Integer getIntDate(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntDate(c);
	}

	/**
	 * 根据Integer获取Date日期
	 * 
	 * @param n
	 * @return
	 */
	public static Date getDate(Integer n) {
		if (n == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(n / 10000, n / 100 % 100 - 1, n % 100);
		return c.getTime();
	}

	/**
	 * 根据年月日生成Calendar
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Calendar newCalendar(int year, int month, int day) {
		Calendar ret = Calendar.getInstance();
		if (year < 100) {
			year = 2000 + year;
		}
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 根据整型日期生成Calendar
	 * @param date
	 * @return
	 */
	public static Calendar newCalendar(int date) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		Calendar ret = Calendar.getInstance();
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 取得Date型的当前日期
	 * @return
	 */
	public static Date getNow() {
		return new Date();
	}
	/**
	 * 整数型日期的减法
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateSub(int date, int days) {
		return intDateAdd(date, -days);
	}
	/**
	 * 整数型日期的加法
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateAdd(int date, int days) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;
		day += days;
		return getIntDate(year, month, day);
	}
}
