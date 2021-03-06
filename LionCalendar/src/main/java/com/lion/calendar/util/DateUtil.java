package com.lion.calendar.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lion-082
 * Todo:
 * Date:  2020/5/28 11:48
 */
public class DateUtil {


    public static String getCNMothon(int month) {
        String strMonth = "";
        switch (month) {
            case 1:
                strMonth = "一月";
                break;
            case 2:
                strMonth = "二月";
                break;
            case 3:
                strMonth = "三月";
                break;
            case 4:
                strMonth = "四月";
                break;
            case 5:
                strMonth = "五月";
                break;
            case 6:
                strMonth = "六月";
                break;
            case 7:
                strMonth = "七月";
                break;
            case 8:
                strMonth = "八月";
                break;
            case 9:
                strMonth = "九月";
                break;
            case 10:
                strMonth = "十月";
                break;
            case 11:
                strMonth = "十一月";
                break;
            case 12:
                strMonth = "十二月";
                break;
        }
        return strMonth;
    }

    public static int getIntMonth(String monthStr) {
        int month = 0;
        switch (monthStr) {
            case "一月":
                month = 1;
                break;
            case "二月":
                month = 2;
                break;
            case "三月":
                month = 3;
                break;
            case "四月":
                month = 4;
                break;
            case "五月":
                month = 5;
                break;
            case "六月":
                month = 6;
                break;
            case "七月":
                month = 7;
                break;
            case "八月":
                month = 8;
                break;
            case "九月":
                month = 9;
                break;
            case "十月":
                month = 10;
                break;
            case "十一月":
                month = 11;
                break;
            case "十二月":
                month = 12;
                break;
        }
        return month;
    }

    /**
     * 获取日期里的号 yyyy-MM-dd
     */
    public static int getDayForDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(time);
            SimpleDateFormat format1 = new SimpleDateFormat("dd");
            String s = format1.format(date);
            return Integer.parseInt(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * 获取日期里的年 yyyy-MM-dd
     */
    public static int getYearForDate(String dateStr) {
        return getYearForDate(dateStr, "yyyy-MM-dd");
    }

    public static int getYearForDateSmart(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return 0;
        String formatStr;
        String dateArr[] = dateStr.split("-");
        if (dateArr.length > 2) {
            formatStr = "yyyy-MM-dd";
        } else {
            formatStr = "yyyy-MM";
        }
        return getYearForDate(dateStr, formatStr);
    }

    /**
     * 获取日期里的年
     *
     * @param dateStr   日期
     * @param formatStr 格式
     */
    public static int getYearForDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = format.parse(dateStr);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
            String s = format1.format(date);
            return Integer.parseInt(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 获取日期里的年月
     *
     * @param dateStr   日期
     * @param formatStr 格式
     */
    public static String getYearMonthForDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = format.parse(dateStr);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
            String s = format1.format(date);
            return s;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getYearMonthForDateSmart(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return "";
        String formatStr;
        String dateArr[] = dateStr.split("-");
        if (dateArr.length > 2) {
            formatStr = "yyyy-MM-dd";
        } else {
            formatStr = "yyyy-MM";
        }
        return getYearMonthForDate(dateStr, formatStr);
    }


    /**
     * 获取日期里的月
     *
     * @param dateStr   日期
     * @param formatStr 格式
     */
    public static Integer getMonthForDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = format.parse(dateStr);
            SimpleDateFormat format1 = new SimpleDateFormat("MM");
            String s = format1.format(date);
            return Integer.parseInt(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Integer getMonthForDateSmart(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return -1;
        String formatStr;
        String dateArr[] = dateStr.split("-");
        if (dateArr.length > 2) {
            formatStr = "yyyy-MM-dd";
        } else {
            formatStr = "yyyy-MM";
        }
        return getMonthForDate(dateStr, formatStr);
    }

    /**
     * string转date
     *
     * @param dateStr
     * @return
     */
    public static Date getStrForDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();

    }


    /**
     * 获取系统年份
     *
     * @return
     */
    public static int getLocalYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //Log.e("--->", "getLocalYear: " + year);
        return year;
    }

    /**
     * 获取系统月份
     *
     * @return
     */
    public static int getLocalMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        //Log.e("--->", "getLocalMonth: " + month);
        return month;
    }


    /**
     * 获取系统日期
     *
     * @return
     */
    public static int getLocalDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //Log.e("--->", "getLocalDay: " + day);
        return day;
    }

    /**
     * 获取当前时间为本月的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekAndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取当前时间为本月的第几天
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        //获取当前时间为本月的第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
//            week = week - 1;
        } else {
            day = day - 1;
        }
//        if (dayOfMonth==1){
//            week=week+1;
//        }
        Log.e("--->", "今天是本月的第" + dayOfMonth + "天，第" + (week) + "周" + ",星期" + (day));
        return week;
    }


    /**
     * 根据日期取得星期几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String week = sdf.format(date);
        return week;
    }

    /**
     * 格式化int 数字不足位数前面补0
     *
     * @param day
     * @return
     */
    public static String formatInt(int day) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(day);
    }
}
