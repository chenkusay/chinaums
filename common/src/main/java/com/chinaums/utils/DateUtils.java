package com.chinaums.utils;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期处理
 * 
 * @author rachel.li
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date formatToDate(Date date, String pattern) {
        String dateStr = format(date, pattern);
        return parse(dateStr, pattern);
    }
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    /**
     * @Title:pase
     * @Description:字符传转化日期格式
     * @param @param date
     * @param @param pattern
     * @param @throws ParseException
     * @return Date
     */
    public static Date parse(String date){
        return parse(date, DATE_PATTERN);
    }
    public static Date parse(String date, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        if (date!=null) {
            try {
                return df.parse(date);
            } catch (ParseException e) {
//				e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 延迟几分钟的日期
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date delayMinute(Date date,int minute){
        Calendar cal= getCalendar(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    public static Date delayYear(Date date, int differYear) {
        Calendar cal = getCalendar(date);
        cal.add(Calendar.YEAR, differYear);
        return cal.getTime();
    }
    public static Date delayDay(Date date, int differDay) {
        Calendar cal = getCalendar(date);
        cal.add(Calendar.DAY_OF_YEAR, differDay);
        return cal.getTime();
    }

    public static Date delayMonth(Date date, int differMonth) {
        Calendar cal = getCalendar(date);
        cal.add(Calendar.MONTH, differMonth);
        return cal.getTime();
    }

    /**
     * @Title:isDateBefore
	 * @Description:目标日期小于源日期
	 * @param @param sourceDate 源日期
	 * @param @param targetDate 目标日期
	 * @return boolean
	 */
    public static boolean isDateBefore(Date sourceDate, Date targetDate) {
        return sourceDate.getTime() > targetDate.getTime() ? true : false;
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 获取过去任意天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static List<String> getPostDate(int intervals) {
        Date now = new Date();
        List<String> pastDaysList = Lists.newArrayList();
        for (int i = intervals-1; i > 0; i--) {
            Date temp = delayDay(now, -i);
            pastDaysList.add(format(temp, "MM-dd"));
        }
        pastDaysList.add(format(now, "MM-dd"));
        return pastDaysList;
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime
     *            时间区间,半闭合,如[10:00-20:00]
     * @param curTime
     *            需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            }
            else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }
    /**
     * @Title:countDaysBetweenDates
     * @Description:获取两个日期间的天数
     * @param @param date1
     * @param @param date2
     * @param @return
     * @return long
     */
    public static long countDaysBetweenDates(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return (between_days < 0) ? -between_days : between_days;
    }
    /**
     * 计算两个日期相差的毫秒数
     */
    public static long calMillisecond(Date date1, Date date2){
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return time2 - time1;
    }

    /**
     * 创建订单号
     * @return String
     */
    public static String createOrdersNo() {
        return "X"+new Date().getTime()+""+(int)(Math.random()*1000);
    }
}
