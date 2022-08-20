package com.maker.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerUtils {
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final int ONE_HOUR_SECOND = 3600;

    public static final int ONE_DAY_SECOND = ONE_HOUR_SECOND * 24;

    /**
     * 今日凌晨零点
     *
     * @return
     */
    public static Date zeroOfToday() {
        return zeroOfDay(new Date());
    }

    /**
     * 昨天凌晨零点
     *
     * @return
     */
    public static Date zeroOfYesterday() {
        Date date = addDay(new Date(), -1);
        return zeroOfDay(date);
    }

    public static Date zeroOfTomorrow() {
        Date date = addDay(new Date(), 1);
        return zeroOfDay(date);
    }

    /**
     * 当月某天
     *
     * @param dayOfMonth
     * @return
     */
    public static Date someDayOfMonth(int dayOfMonth) {
        return someDayOfMonth(new Date(), dayOfMonth);
    }

    /**
     * 当月某天
     *
     * @param dayOfMonth
     * @return
     */
    public static Date someDayOfMonth(Date date, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某个月的第一天
     *
     * @param date
     * @return
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return zeroOfDay(calendar.getTime());
    }

    /**
     * 获取某个月的最后一天凌晨
     *
     * @param date
     * @return
     */
    public static Date lastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return zeroOfDay(calendar.getTime());
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static int getMonthMaxDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth());
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 某日零点
     *
     * @param date
     * @return
     */
    public static Date zeroOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static Date parse(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date addWeek(Date date, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH, week);
        return calendar.getTime();
    }


    public static int daysOfTwo(Date fDate, Date oDate) {
        int days = (int) ((oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    public static void main(String[] args) {
        Date date = TimerUtils.parse("2017-11-30", YYYYMMDD);
        date = addMonth(date, 1);
        System.out.println(format(date, YYYYMMDDHHMMSS));
    }
}
