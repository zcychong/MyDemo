package com.healthmanage.ylis.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 时间操作类
 * 
 * @author cxw
 *
 */
public class DateOperate {
	private static String tag = "DateOperate";

	/**
	 * 获取系统当前年份，格式:yyyy
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataYear() {
		DateFormat df = new SimpleDateFormat("yyyy");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前年份的月份，格式:MM
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataMonth() {
		DateFormat df = new SimpleDateFormat("MM");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前年份的月份的日期，格式:dd
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataDay() {
		DateFormat df = new SimpleDateFormat("dd");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前年份的月份的小时，格式:HH
	 *
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataHours() {
		DateFormat df = new SimpleDateFormat("HH");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前年份的月份的日期，格式:mm
	 *
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataMumnit() {
		DateFormat df = new SimpleDateFormat("mm");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前日期，格式:yyyyMMddHHmmss
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前日期，格式:yyyyMMdd
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentData() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前日期，格式:yyyy年MM月dd日
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataC() {
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前日期，格式:yyyy-MM-dd
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDataWithSpe() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str = df.format(new Date());
		return str;
	}

	/**
	 * 获取系统当前日期，格式:yyyyMMdd000000
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String str = df.format(new Date());
		return str + "000000";
	}

	/**
	 * 获取系统当前月份，格式:yyyyMM
	 * 
	 * @author cxw
	 * @return
	 */
	public static String getCurrentMonth() {
		DateFormat df = new SimpleDateFormat("yyyyMM");
		return df.format(new Date());
	}

	/**
	 * 根据日期字符串，转换成带有年月日汉字的字符串
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getDateWithChinese(String dateStr) {
		String newDate = dateStr;
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			newDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
		} catch (ParseException pe) {
			Log.e(tag, pe.toString());
		}
		return newDate;
	}

	/**
	 * 根据日期字符串，转换成带有年月日汉字的字符串
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getDateWithMonthAndDay(String dateStr) {
		String newDate = dateStr;
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			newDate = new SimpleDateFormat("M/d").format(date);
		} catch (ParseException pe) {
			Log.e(tag, pe.toString());
		}
		return newDate;
	}

	/**
	 * 获取系统传入日期字符串的第i天日期，格式：yyyyMMdd000000
	 * 
	 * @author cxw
	 * @param oneDate
	 *            格式要求为yyyyMMddhhmmss或者yyyyMMdd,i 整数（正负都可以）
	 * @return
	 */
	public static String getCurrentDateByParai(String oneDate, int i) {
		String str = "";
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(oneDate.substring(0, 8));
			c.setTime(d);
			c.add(Calendar.DATE, i);
			Date date = c.getTime();
			str = df.format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return str + "000000";
	}

	/**
	 * 获取系统传入日期字符串的第i天日期，格式：yyyy-MM-dd
	 * 
	 * @author cxw
	 * @param oneDate
	 *            yyyy-MM-dd,i 整数（正负都可以）
	 * @return
	 */
	public static String countDateOther(String oneDate, int i) {
		String str = "";
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d = df.parse(oneDate);
			c.setTime(d);
			c.add(Calendar.DATE, i);
			Date date = c.getTime();
			str = df.format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return str;
	}

	/**
	 * 获取系统传入日期字符串的第i天日期，格式：yyyyMMdd
	 * 
	 * @author cxw
	 * @param oneDate
	 *            yyyyMMdd,i 整数（正负都可以）
	 * @return
	 */
	public static String countDate(String oneDate, int i) {
		String str = "";
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(oneDate);
			c.setTime(d);
			c.add(Calendar.DATE, i);
			Date date = c.getTime();
			str = df.format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return str;
	}

	/**
	 * 获取系统传入日期字符串的前或后第n天日期，格式：yyyyMMdd000000
	 * 
	 * @author cxw
	 * @param oneDate
	 *            格式要求为yyyyMMddhhmmss或者yyyyMMdd
	 * @return
	 */
	public static String getCurrentDateComplute(String oneDate, int days) {
		String str = "";
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(oneDate.substring(0, 8));
			c.setTime(d);
			c.add(Calendar.DATE, days);
			Date date = c.getTime();
			str = df.format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return str + "000000";
	}

	/**
	 * 根据传入的日期字符串获取其之前7天内的日期
	 * 
	 * @author cxw
	 * @param dateStr
	 *            格式要求：yyyyMMdd或者yyyyMMddhhmmss
	 * @return 格式要求: MM/dd
	 */
	public static String[] getBefore7(String dateStr) {
		String[] strs = new String[7];
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(dateStr.substring(0, 8));
			Calendar c = Calendar.getInstance();
			// Calendar.DATE指示一个月中的某天 add方法根据日历的规则，为给定的日历字段添加或减去指定的时间量
			for (int i = 1; i < 8; i++) {
				c.setTime(d);
				c.add(Calendar.DATE, -i + 1);
				Date date = c.getTime();
				SimpleDateFormat df2 = new SimpleDateFormat("MM/dd");
				String str = df2.format(date);
				strs[i - 1] = str;
			}
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return strs;
	}

	/**
	 * 根据传入的日期字符串获取其之前7天内的日期
	 * 
	 * @author cxw
	 * @param dateStr
	 *            格式要求：yyyyMMdd或者yyyyMMddhhmmss
	 * @return 格式要求: MM/dd 在日期后面追加1、2、3以区别早上、中午、晚上
	 */
	public static String[] getNibpBefore7(String dateStr) {
		String[] strs = new String[21];
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(dateStr.substring(0, 8));
			Calendar c = Calendar.getInstance();
			// Calendar.DATE指示一个月中的某天 add方法根据日历的规则，为给定的日历字段添加或减去指定的时间量
			for (int i = 1; i < 8; i++) {
				c.setTime(d);
				c.add(Calendar.DATE, -i + 1);
				Date date = c.getTime();
				SimpleDateFormat df2 = new SimpleDateFormat("MM/dd");
				String str = df2.format(date);
				strs[i * 3 - 1] = str + "080000";
				strs[i * 3 - 2] = str + "160000";
				strs[i * 3 - 3] = str + "240000";
			}
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return strs;
	}

	/**
	 * 根据传入的日期字符串获取其之后14天内的日期
	 * 
	 * @author cxw
	 * @param dateStr
	 *            格式要求：yyyyMMdd或者yyyyMMddhhmmss
	 * @return 格式要求: MM/dd
	 */
	public static String[] getAfter14(String dateStr) {
		String[] strs = new String[14];
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(dateStr.substring(0, 8));
			Calendar c = Calendar.getInstance();
			// Calendar.DATE指示一个月中的某天 add方法根据日历的规则，为给定的日历字段添加或减去指定的时间量
			// for(int i=14; i>=1; i--){
			for (int i = 1; i < 15; i++) {
				c.setTime(d);
				c.add(Calendar.DATE, i);
				Date date = c.getTime();
				SimpleDateFormat df2 = new SimpleDateFormat("MM/dd");
				String str = df2.format(date);
				strs[14 - i] = str;
			}
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		}
		return strs;
	}

	/**
	 * 将一位的数字，前补0
	 * 
	 * @param one
	 * @return
	 */
	public static String toBoth(String one) {
		if (one.length() == 1) {
			return "0" + one;
		}
		return one;
	}

	/**
	 * 获取当前月份的天数集合yyyyMMdd
	 * 
	 * @return
	 */
	public static String[] getCurrentDays() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		int length = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String[] days = new String[length];
		String yearMonth = new SimpleDateFormat("yyyyMM").format(new Date());
		for (int i = 0; i < length; i++) {
			days[i] = yearMonth + String.format("%02d", i + 1);
		}
		return days;
	}

	/**
	 * 时间格式转换yyMMdd---yy/MM/dd
	 */
	public static String changeFormat(String str) {
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(str);
			str = new SimpleDateFormat("yyyy/MM/dd").format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * 时间格式转换yyMMdd---yy-MM-dd
	 */
	public static String changeFormat2(String str) {
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(str);
			str = new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * 时间格式转换yyMMdd---yy年MM月dd日
	 */
	public static String changeFormat3(String str) {
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(str);
			str = new SimpleDateFormat("yyyy年MM月dd日").format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * 时间格式转换yyMMddHHmmss---yy-MM-dd HH:mm:ss
	 */
	public static String changeFormatDateTime(String str) {
		if ("".equals(str))
			return "";
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
			str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * @方法描述：将时间戳转根据dateOption 格式化
	 * @param String
	 *            timestamp, String dateOption
	 * @return
	 */
	public static String getFormatDateTimme(long timestamp, String dateOption) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateOption);
			Date date = new Date(timestamp);
			return sdf.format(date);

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 时间格式转换yyMMddHHmmss---yy-MM-dd
	 */
	public static String changeFormatDateDate(String str) {
		if ("".equals(str))
			return "";
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
			str = new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * 返回汉字：今天、昨天，否则返回空串
	 * 
	 * @param str
	 *            格式：yyyy-MM-dd
	 * @return
	 */
	public static String judgeDate(String str) {
		if (str.equals(getCurrentDataWithSpe())) {
			return "今天";
		} else if (str.equals(countDateOther(getCurrentDataWithSpe(), -1))) {
			return "昨天";
		} else {
			return "";
		}
	}

	/**
	 * 获取当前时间（毫秒数形式）
	 * 
	 * @return
	 */
	public static long getTime() {
		return new Date().getTime();
	}

	/**
	 * 获取显示时间：非今天显示月日，今天显示时分
	 * 
	 * @return
	 */
	public static String getShowTime(String dateStr) {
		try {
			String curDate = getCurrentData();
			Date date = new SimpleDateFormat("yyyyMMddhhmmss").parse(dateStr);
			if (curDate.equals(dateStr.substring(0, 8))) {// 判断是否为今天
				dateStr = new SimpleDateFormat("HH:mm").format(date);
			} else {
				dateStr = new SimpleDateFormat("MM月dd日").format(date);
			}
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return dateStr;
		}
	}

	/**
	 * 根据计算间隔月份，返回yyyyMMdd
	 * 
	 * @author cxw
	 * @param monthGap
	 * @return
	 */
	public static String computeDMonth(String date, int monthGap) {
		String str = "";
		try {
			Calendar c = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(date);
			c.setTime(d);
			c.add(Calendar.MONTH, monthGap);
			Date da = c.getTime();
			str = df.format(da);
		} catch (Exception e) {
			Log.e(tag, e.toString());
		} finally {
			return str;
		}
	}

	/**
	 * 计算两个日期之间的间隔天数
	 * 
	 * @author cxw
	 * @param oneDate
	 *            yyyyMMdd, twoDate yyyyMMdd
	 * @return
	 */
	public static long computeDayGap(String oneDate, String twoDate) {
		long gap = 0;
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date oned = df.parse(oneDate);
			c.setTime(oned);
			long oneLong = c.getTimeInMillis();
			Date twod = df.parse(twoDate);
			c.setTime(twod);
			long twoLong = c.getTimeInMillis();
			gap = (twoLong - oneLong) / (24 * 3600 * 1000);
		} catch (ParseException e) {
			Log.e(tag, e.toString());
		} finally {
			return gap;
		}
	}

	/**
	 * @方法描述：计算当前时间和初始时间相差的秒数
	 * @param initDate
	 * @return
	 */
	@SuppressWarnings("finally")
	public static long computeTwoTimeSec(String initDate) {
		long second = 0;
		try {

			long currTime = getTime();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = df.parse(initDate);

			second = (currTime - d.getTime()) / 1000;

		} catch (Exception e) {
			Log.e(tag, e.toString());
		} finally {
			return second;
		}
	}

	/**
	 * @方法描述：计算时间差
	 * @param initDate
	 * @return
	 */
	@SuppressWarnings("finally")
	public static long computeDiffTime(String initDate) {
		long month = 0;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = df.parse(initDate);

			Date curr = new Date();
			String data = df.format(curr);
			Log.e("计算时间差", initDate + "-" + data);
			month = (d.getTime() - df.parse(data).getTime()) / 1000 / 60;

		} catch (Exception e) {
			Log.e(tag, e.toString());
		} finally {
			return month;
		}
	}

	/**
	 * 将14位格式的时间 转换为 yy-mm-dd hh:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static String changeTimeType(String str) {
		String time = "";
		if (str.length() != 14) {
			return str;
		} else {
			time = str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
					+ str.substring(6, 8) + " " + str.substring(8, 10) + ":"
					+ str.substring(10, 12) + ":" + str.substring(12, 14);
			return time;
		}

	}

	/**
	 * 获取两个日期的月份差
	 * 
	 * @param nowday
	 * @param birthday
	 * @return
	 */
	public static float getDiffMouth(String nowday, String birthday) {
		float month = 0;
		int nowYear = Integer.valueOf(nowday.substring(0, 4));
		int nowMouth = Integer.valueOf(nowday.substring(5, 7));
		int nowDay = Integer.valueOf(nowday.substring(8, 10));

		int birYear = Integer.valueOf(birthday.substring(0, 4));
		int birMouth = Integer.valueOf(birthday.substring(5, 7));
		int birDay = Integer.valueOf(birthday.substring(8, 10));

		if (nowYear > birYear) {
			month += (nowYear - birYear) * 12;
		}

		month += (nowMouth - birMouth);

		if (nowDay < birDay) {
			month--;
		}
		return month;
	}

	/**
	 * 获取两个日期的小于一个月的天数差
	 * 
	 * @param nowday
	 * @param birthday
	 * @return
	 */
	public static int getDiffDay(String nowday, String birthday) {
		int nowDay = Integer.valueOf(nowday.substring(8, 10));
		int birDay = Integer.valueOf(birthday.substring(8, 10));
		if (nowDay >= birDay) {
			return (nowDay - birDay);
		} else {
			return (30 + nowDay - birDay);
		}
	}

}
