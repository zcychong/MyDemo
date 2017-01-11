package com.healthmanage.ylis.common;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	public static final String NIBP_DEVICE_NAME = "NIBP";// 血压设备名
	public static final String SPO_DEVICE_NAME = "SpO";// 血氧设备名
	public static final String BG_DEVICE_NAME = "BG";// 血糖设备名
	public static final String PULM_DEVICE_NAME = "PULM";// 肺活量设备名
	public static final String BC_DEVICE_NAME = "BC";// 尿液分析设备名
	public static final String TEMP_DEVICE_NAME = "TEMP";// 耳温设备名


	// 数据库表名
	public static final String DEVICE_TABLE_NAME = "device_table";
	public static final String USER_TABLE_NAME = "user_table";
	public static final String NIBP_TABLE_NAME = "nibp_table";
	public static final String SPO_TABLE_NAME = "spo_table";
	public static final String BG_TABLE_NAME = "bg_table";
	public static final String PULM_TABLE_NAME = "pulm_table";
	public static final String BC_TABLE_NAME = "bc_table";
	public static final String TEMP_TABLE_NAME = "temp_table";
	public static final String GROUP_TABLE_NAME = "group_table";

	public static final String HT_WT_TABLE_NAME = "ht_wt_table";

	// SharedPreferences文件名
	public static final String FILE_NAME = "ecare";
	// 有新数据存储到xml文件 的key
	public static final String NIBP_NEW_DATA = "nibp_new_data";
	public static final String SPO_NEW_DATA = "spo_new_data";
	public static final String BG_NEW_DATA = "bg_new_data";
	public static final String PULM_NEW_DATA = "pulm_new_data";
	public static final String BC_NEW_DATA = "bc_new_data";
	public static final String TEMP_NEW_DATA = "temp_new_data";
	public static final String HTWT_NEW_DATA = "htwt_new_data";

	public static boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}

	public static boolean isNotEmpty(String str) {
		return (str != null && str.trim().length() > 0 && !str.equals("null"));
	}

	/**
	 * @方法描述：验证手机号是否符合正则表达式，正确返回true，否则返回false
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(14[5,7])|(18[0-9])|(17[0,3,6,7,8]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * @方法描述：验证身份证是否符合这则表达式，正确返回true，否则返回false
	 * @param iDNum
	 * @return
	 */
	public static boolean isIdNum(String iDNum) {
		Pattern p = Pattern
				.compile("^([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))|([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})$");
		Matcher m = p.matcher(iDNum);
		return m.matches();
	}

	/**
	 * 隐藏身份证号码
	 * 
	 * @param idCard
	 *            //身份证号码
	 */
	public static String dismissIdCard(String idCard) {
		String temp = idCard;
		if (!isEmpty(idCard)) {
			if (idCard.length() == 18) {
				temp = idCard.substring(0, 4) + "***"
						+ idCard.substring(14, idCard.length());
			}
		}
		return temp;
	}

	public static String getAgeFromIdCard(String idCard) {
		int year, month, day, idLength = idCard.length();
		Calendar cal1 = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		if (idLength == 18) {
			year = Integer.parseInt(idCard.substring(6, 10));
			month = Integer.parseInt(idCard.substring(10, 12));
			day = Integer.parseInt(idCard.substring(12, 14));
		} else if (idLength == 15) {
			year = Integer.parseInt(idCard.substring(6, 8)) + 1900;
			month = Integer.parseInt(idCard.substring(8, 10));
			day = Integer.parseInt(idCard.substring(10, 12));
		} else {
			System.out.println("This ID card number is invalid!");
			return String.valueOf("-1");
		}
		cal1.set(year, month, day);
		return String.valueOf(getYearDiff(today, cal1));
	}

	static int getYearDiff(Calendar cal, Calendar cal1) {
		int m = (cal.get(cal.MONTH)) - (cal1.get(cal1.MONTH));
		int y = (cal.get(cal.YEAR)) - (cal1.get(cal1.YEAR));
		return (y * 12 + m) / 12;
	}

	// 判断一个字符串是否含有数字
	public static boolean hasDigit(String content) {
		boolean flag = false;
		if (content == null) {
			return flag;
		}

		for (int i = 0; i < content.length(); i++) {
			if (Character.isDigit(content.charAt(i))) { // 用char包装类中的判断数字的方法判断每一个字符
				flag = true;
				return flag;
			}
		}
		return flag;
	}
}
