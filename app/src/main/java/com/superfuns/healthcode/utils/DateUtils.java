package com.superfuns.healthcode.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 时间工具类（时间格式转换方便类）
 */
public class DateUtils {

	public static SimpleDateFormat hmsSDF = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat hmSDF = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat ymdhmsSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat ymdSDF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 返回一定格式的当前时间
	 *
	 * @param "yyyy-MM-dd HH:mm:ss E"
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		String dateString = ymdhmsSDF.format(date);
		return dateString;

	}

	//获取当前日期
	public static long getTodayDate() {
		Date date = new Date();
		String dateString = ymdSDF.format(date);
		Log.i("TAG","dateString:"+dateString);
		long today = 0;
		try {
			today = ymdSDF.parse(dateString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return today;
	}

	//获取两周前的时间
	public static long getTwoWeekDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -7);
		Date date = c.getTime();
		String  dateString = ymdSDF.format(date);
		long twoWeeks = 0;
		try {
			twoWeeks = ymdSDF.parse(dateString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return twoWeeks;
	}

	public static long getDateMillis(String dateString, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		long millionSeconds = 0;
		try {
			millionSeconds = sdf.parse(dateString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}// 毫秒

		return millionSeconds;
	}

	/**
	 * 格式化输入的millis
	 *
	 * @param millis
	 * @param pattern yyyy-MM-dd HH:mm:ss E
	 * @return
	 */
	public static String dateFormat(long millis, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = new Date(millis);
		String dateString = simpleDateFormat.format(date);
		return dateString;
	}

	/**
	 * 将dateString原来old格式转换成new格式
	 *
	 * @param dateString
	 * @param oldPattern yyyy-MM-dd HH:mm:ss E
	 * @param newPattern
	 * @return oldPattern和dateString形式不一样直接返回dateString
	 */
	public static String dateFormat(String dateString, String oldPattern,
									String newPattern) {
		long millis = getDateMillis(dateString, oldPattern);
		if (0 == millis) {
			return dateString;
		}
		String date = dateFormat(millis, newPattern);
		return date;
	}

	/**
	 * +     * 特别专用的
	 * +     *
	 * +     * @param dateString
	 * +     * @return
	 * +
	 */
	public static long getSpecialDateMillis(String dateString) {

		long millionSeconds = 0;
		try {
			millionSeconds = hmsSDF.parse(dateString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}// 毫秒

		return millionSeconds;
	}

	public static String getSpecialCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		String dateString = hmsSDF.format(date);
		return dateString;
	}
}
