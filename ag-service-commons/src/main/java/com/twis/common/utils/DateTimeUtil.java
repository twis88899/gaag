package com.twis.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.ConversionException;

public class DateTimeUtil {
	
	public static final String DATE_DIVISION = "-";
	public static final String TIME_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYYMMDD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String DATA_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static final String TIME_PATTERN_HHMMSS = "HH:mm:ss";
	public static final String TIME_PATTERN_HHMM = "HH:mm";

	public static final int ONE_SECOND = 1000;
	public static final int ONE_MINUTE = 60 * ONE_SECOND;
	public static final int ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_DEFAULT);

	/**
	 * Return the current date
	 * 
	 * @return － DATE<br>
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return currDate;
	}

	/**
	 * Return the current date string
	 * 
	 * @return － 产生的日期字符串<br>
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate);
	}

	/**
	 * Return the current date in the specified format
	 * 
	 * @param strFormat
	 * @return
	 */
	public static String getCurrentDateStr(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate, strFormat);
	}

	public static synchronized Date getTime(final long millis) {
		Date date = new Date();
		if (millis > 0) {
			date.setTime(millis);
		}
		return date;
	}

	/**
	 * Parse a string and return a date value
	 * 
	 * @param dateValue
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String dateValue) {
		return parseDate(DATE_PATTERN_DEFAULT, dateValue);
	}
	
	public static Date parseDayDateByAuto(String dataValue) {
		if ((dataValue != null) && (dataValue.length() > 0)) {
			if (dataValue.indexOf("/") > 0)
				return DateTimeUtil.parseDate("yyyy/MM/dd", dataValue);
			if (dataValue.indexOf("-") > 0)
				return DateTimeUtil.parseDate("yyyy-MM-dd", dataValue);
		}
		return DateTimeUtil.getCurrentDate();
	}
	
	
	public static Date parseDateTimeByAuto(String dataValue) {
		if ((dataValue != null) && (dataValue.length() > 0)) {
			if (dataValue.indexOf("/") > 0)
				return DateTimeUtil.parseDate("yyyy/MM/dd HH:mm:ss", dataValue);
			if (dataValue.indexOf("-") > 0)
				return DateTimeUtil.parseDate("yyyy-MM-dd HH:mm:ss", dataValue);
		}
		return DateTimeUtil.getCurrentDate();
	}

	/**
	 * Parse a strign and return a datetime value
	 * 
	 * @param dateValue
	 * @return format(yyyy-MM-dd HH:mm:ss)
	 */
	public static Date parseDateTime(String dateValue) {
		return parseDate(TIME_PATTERN_DEFAULT, dateValue);
	}

	/**
	 * Parse a string and return the date value in the specified format
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date parseDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;

		if (strFormat == null)
			strFormat = TIME_PATTERN_DEFAULT;

		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;

		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String format(Date aTs_Datetime) {
		return format(aTs_Datetime, DATE_PATTERN_DEFAULT);
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatTime(Date aTs_Datetime) {
		return format(aTs_Datetime, TIME_PATTERN_DEFAULT);
	}

	/**
	 * 将Date类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Date aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;
		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Format
	 * @return
	 */
	public static String formatTime(Date aTs_Datetime, String as_Format) {
		if (aTs_Datetime == null || as_Format == null)
			return null;
		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Format);

		return dateFromat.format(aTs_Datetime);
	}

	public static String getFormatTime(Date dateTime) {
		return formatTime(dateTime, TIME_PATTERN_HHMMSS);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Timestamp aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;
		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * 取得指定日期N天后的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	public static long minsBetweenOfLong(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_mins = (time2 - time1) / (1000 *60);
		return between_mins;
	}
	
	
	public static long secondsBetweenOfLong(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_mins = (time2 - time1) / (1000);
		return between_mins;
	}
	
	public static int minsBetween(Date date1, Date date2) {
		return Integer.parseInt(String.valueOf(minsBetweenOfLong(date1, date2)));
	}
	
	/**
	 * 计算两个日期相差的月数
	 */
	public static int monthsBetween(Date date1, Date date2)
            throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        
        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }
       
        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
    }

	/**
	 * 计算当前日期相对于"1977-12-01"的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long getRelativeDays(Date date) {
		Date relativeDate = DateTimeUtil.parseDate("yyyy-MM-dd", "1977-12-01");

		return DateTimeUtil.daysBetween(relativeDate, date);
	}

	public static Date getDateBeforTwelveMonth() {
		String date = "";
		Calendar cla = Calendar.getInstance();
		cla.setTime(getCurrentDate());
		int year = cla.get(Calendar.YEAR) - 1;
		int month = cla.get(Calendar.MONTH) + 1;
		if (month > 9) {
			date = String.valueOf(year) + DATE_DIVISION + String.valueOf(month) + DATE_DIVISION + "01";
		} else {
			date = String.valueOf(year) + DATE_DIVISION + "0" + String.valueOf(month) + DATE_DIVISION + "01";
		}

		Date dateBefore = parseDate(date);
		return dateBefore;
	}

	/**
	 * 传入时间字符串,加一天后返回Date
	 * 
	 * @param date
	 *            时间 格式 YYYY-MM-DD
	 * @return
	 */
	public static Date addDate(String date) {
		if (date == null) {
			return null;
		}
		Date tempDate = parseDate(DATE_PATTERN_DEFAULT, date);
		String year = format(tempDate, "yyyy");
		String month = format(tempDate, "MM");
		String day = format(tempDate, "dd");

		GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

		calendar.add(GregorianCalendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date getDate(Date date, int num) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, num); // 设置为前/后一天

		return calendar.getTime();
	}

	public static String getDateToString(Date date, int num, String pattern) {
		Date d = getDate(date, num);
		return parseDateToString(d, pattern);
	}

	public static String getDateByMonthToString(Date date, int num, String pattern) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.MONTH, num); // 设置为前/后一个月
		return parseDateToString(calendar.getTime(), pattern);
	}

	public static Date getDateByHour(Date date, int num) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.HOUR, num); // 设置为前/后小时

		return calendar.getTime();
	}
	
	public static Date getDateByYear(Date date, int num) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.YEAR, num); // 设置为前/后年

		return calendar.getTime();
	}

	public static Date parseDate(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		String dateStr = df.format(date);
		return parseDate(pattern, dateStr);
	}

	public static String parseDateToString(Date date, String pattern) {
		Date d = parseDate(date, pattern);
		DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);

		return df.format(d);
	}

	public static String parseDateToString(Date date, String pattern, Locale locale) {
		Date d = parseDate(date, pattern);
		DateFormat df = new SimpleDateFormat(pattern, locale);

		return df.format(d);
	}

	public static Date getDate() {
		return new Date();
	}

	/**
	 * 获取某日期是几号
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		return today;
	}

	/**
	 * 获取某日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取某日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayOfWeekString(Date date) {
		int dayOfWeek = getDayOfWeek(date);
		switch (dayOfWeek) {
		case 1:
			return "Sunday";
		case 2:
			return "Monday";
		case 3:
			return "Tuesday";
		case 4:
			return "Wednesday";
		case 5:
			return "Thursday";
		case 6:
			return "Friday";
		case 7:
			return "Saturday";
		}
		return null;
	}

	/**
	 * 获取某日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayOfWeekShortString(Date date) {
		int dayOfWeek = getDayOfWeek(date);
		switch (dayOfWeek) {
		case 1:
			return "Sun";
		case 2:
			return "Mon";
		case 3:
			return "Tue";
		case 4:
			return "Wed";
		case 5:
			return "Thu";
		case 6:
			return "Fri";
		case 7:
			return "Sat";
		}
		return null;
	}

	public static Date addWeeks(Date date, int num) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.WEEK_OF_MONTH, num);

		return cal.getTime();
	}

	public static Date addHours(Date date, int num) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.HOUR_OF_DAY, num);

		return cal.getTime();
	}

	public static Date addMonths(Date date, int num) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.MONTH, num);

		return cal.getTime();
	}

	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}

	public static int getMins(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 
	 * @param date
	 * @return k->yyyy-MM-dd,v->MMM
	 */
	public static Map<String, String> getMonthOfYearMap(Date date) {
		Map<String, String> dateMap = new LinkedHashMap<String, String>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);

		Date d = calendar.getTime(); // first month of year

		for (int i = 0; i < 12; i++) {
			Date resultDate = DateTimeUtil.addMonths(d, i);

			String key = DateTimeUtil.format(resultDate, "yyyy-MM");

			DateFormat df = new SimpleDateFormat("MMM", Locale.ENGLISH);
			String value = df.format(resultDate);

			dateMap.put(key, value);
		}

		return dateMap;
	}

	public static String parseDate2(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	public static Date getDateStart(Date date) {
		String dateStr = parseDate2(date, "yyyy-MM-dd");
		dateStr = dateStr + " 00:00:00";
		return parseDateTime(dateStr);
	}

	public static Date getDateEnd(Date date) {
		String dateStr = parseDate2(date, "yyyy-MM-dd");
		dateStr = dateStr + " 23:59:59";
		return parseDateTime(dateStr);
	}

	public static int get24Hours(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int get12Hours(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR);
	}
	
	public static int getCurYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	public static String getCurYearAndMonth() {
		Calendar c = Calendar.getInstance();
		return String.valueOf(c.get(Calendar.YEAR))+String.valueOf(getMonth(new Date()));
	}
	
	public static Date getAfterDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
		return calendar.getTime();
	}
	
	public static List<String> getAfterNDate(Date date, int days) {
		List<String> list = new ArrayList<String>();
		for(int i=0;i<days;i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + i);
			Date finalDate = calendar.getTime();
			list.add(dateFormat.format(finalDate));
		}
		return list;
	}

	public static Date getBeforeDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
		return calendar.getTime();
	}
	
	public static Date getDateByDiffDays(int days) {
		return getBeforeDate(getCurrentDate(), 0-days);
	}
	
	public static Date getAfterHour(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
		return calendar.getTime();
	}
	
	public static Date getAfterMin(Date date, int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + min);
		return calendar.getTime();
	}
	
	
	
	
	
	/**
	  * @Fields patterns 
	  */
	private static String[] patterns = new String[] { "yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd" };
	
	/**
	  * linxiudong
	  * @Description: 自动尝试转换日期
	  * @param dateStr
	  * @return Date	
	  */
	public static Date parseDateByAuto(String dateStr) throws Exception{
		Exception firstEx = null;
		// 根据patterns中表达式来转换
		for (int i = 0; i < patterns.length; i++) {		
            try {
                DateFormat format = new SimpleDateFormat(patterns[i]);
                return format.parse(dateStr);
            } catch (Exception ex) {
                if (firstEx == null) {
                    firstEx = ex;
                }
            }
        }
		// 如果转换不了尝试当作毫秒数来转换
		// VaccinateDate=\/Date(1440086400000+0800)\/
		try {
			String str = dateStr.substring(dateStr.indexOf("(")+1,dateStr.indexOf(")"));
		    String time = str.substring(0,str.length()-5);
		    String timeZone = str.substring(str.length()-5, str.length());
		    Date date = new Date(Long.parseLong(time));
		    return date;
		}catch(Exception e){}
		
	    
        if (patterns.length > 1) {
            throw new ConversionException("Error converting '" + dateStr.getClass().getTypeName() + "' to '" + Date.class.getTypeName()
                    + "' using  patterns '" + toStrong(patterns) + "'");
        } else {
            throw firstEx;
        }
	}
	
	private static String toStrong(String[] patterns) {
		StringBuilder sb = new StringBuilder();
		for (String str : patterns) {
			sb.append(str+" ; ");
		}
		return sb.toString();
	}
	
	
	//获限指定日期
	public static Date getDateBySpecify(int year, int month, int day)  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");   
		try{
			return sdf.parse(String.format("%d-%d-%d", year, month, day));  
		}
		catch(ParseException e){
			
			return new Date();
		}
	}
	
	//获得当天0点时间 
	public static Date getTimesmorning() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime();
	} 
	
	//获得当天24点时间 
	public static Date getTimesnight() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 24); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime();
	}
	
	//获得本周一0点时间 
	public static Date getTimesWeekmorning() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		return cal.getTime();
	}
	
	//获得本周日24点时间 
	public static Date getTimesWeeknight() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		return cal.getTime();
	} 
	
	//获得本月第一天0点时间 
	public static Date getTimesMonthmorning() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0); 
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
		return cal.getTime();
	} 
	
	//获得本月最后一天24点时间 
	public static Date getTimesMonthnight() { 
		Calendar cal = Calendar.getInstance(); 
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0); 
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		cal.set(Calendar.HOUR_OF_DAY, 24); 
		return cal.getTime(); 
	}
	
	/**
	 * 获取当月最后一天
	 * @return
	 */
	public static String getLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date lastDay = calendar.getTime();
		SimpleDateFormat dateFromat = new SimpleDateFormat(DATE_PATTERN_DEFAULT);
		return dateFromat.format(lastDay);
	}
	/**
	 * @author xukai
	 * 判断是否是当天
	 * */
	public static boolean compareDate(Date date) {
		SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd");
		if (dateFromat.format(new Date()).equals(dateFromat.format(date))){
			return true;
		}
		return false;
	}
	
	public static String getDateTimeOfDescStr(Date date) {
		if (date == null) {
			return "";
		}
		
		Long temp = minsBetweenOfLong(getCurrentDate(), date);
		
		if (temp < 60) {
			return String.valueOf(temp) + "分钟前";
		} else if (temp > 60 && temp / 60 / 24 < 1) {
			return (temp / 60) + "小时前";
		} else if (temp > 60 && temp / 24 >= 1) {
			if (temp / 60 / 24 == 1) {
				return "昨天";
			} else if (temp / 60 / 24 == 1) {
				return "前天";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(date);
			}
		}
		return String.valueOf(temp);
	}
	
	/**
	 * 当月所有日期
	 * @return
	 * @throws Exception
	 */
	public static List<String> getDayListOfMonth() throws Exception {
	    List<String> list = new ArrayList<String>();
	    Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
	    int year = aCalendar.get(Calendar.YEAR);//年份
	    int month = aCalendar.get(Calendar.MONTH)+1;//月份
	    int day = aCalendar.getActualMaximum(Calendar.DATE);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    for (int i = 1; i <= day; i++) {
	        String aDate = String.valueOf(year)+"-"+month+"-" + String.valueOf(i);
	        Date date = sdf.parse(aDate);
	        list.add(sdf.format(date));
	    }
	    return list;
	}
	
	/**
	 * 当月多少天
	 * @return
	 * @throws Exception
	 */
	public static int getDaysOfMonth() throws Exception {
	    List<String> list = new ArrayList<String>();
	    Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
	    int year = aCalendar.get(Calendar.YEAR);//年份
	    int month = aCalendar.get(Calendar.MONTH)+1;//月份
	    int day = aCalendar.getActualMaximum(Calendar.DATE);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    for (int i = 1; i <= day; i++) {
	        String aDate = String.valueOf(year)+"-"+month+"-" + String.valueOf(i);
	        Date date = sdf.parse(aDate);
	        list.add(sdf.format(date));
	    }
	    return list.size();
	}
	
	public static void main(String[] args) throws Exception {
		List<String> list = getAfterNDate(new Date(),7);
		System.out.println(list);
	}

}
