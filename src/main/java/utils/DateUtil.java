package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: youxingyang
 * @date: 2018/4/25 10:02
 */
public final class DateUtil {
    private DateUtil() {
    }

    public static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NOWPATTERN = "yyyyMMddHHmmss";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DD = "dd";

    /**
     * 格式化日期对象
     *
     * @param date
     * @return
     */
    public static Date date2date(Date date) {
        return date2date(date, ISO8601_PATTERN);
    }

    /**
     * 格式化日期对象
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static Date date2date(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String str = sdf.format(date);
        //应该统一采用gmt时区
        //注意数据迁移
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 时间对象转换成 ISO8601字符串
     *
     * @param date
     * @return
     */
    public static String date2string(Date date) {
        return date2string(date, ISO8601_PATTERN);
    }

    /**
     * 时间对象转换成字符串
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String date2string(Date date, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(date);
        return strDate;
    }

    /**
     * 字符串转换成时间对象
     *
     * @param dateString
     * @param formatStr
     * @return
     */
    public static Date string2date(String dateString, String formatStr) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return formateDate;
    }

    /**
     * 时间字符串转换成指定时间格式
     *
     * @param dateString
     * @param formatStr
     * @return
     */
    public static String string2format(String dateString, String formatStr) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return date2string(formateDate, formatStr);
    }

    /**
     * 获得当前年份
     *
     * @return
     */
    public static String getNowYear() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY);
        return sdf.format(new Date());
    }

    /**
     * 获得当前月份
     *
     * @return
     */
    public static String getNowMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(MM);
        return sdf.format(new Date());
    }

    /**
     * 获得当前日期中的日
     *
     * @return
     */
    public static String getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(DD);
        return sdf.format(new Date());
    }

    /**
     * 指定时间距离当前时间的中文信息
     *
     * @param time
     * @return
     */
    public static String getLnowStr(long time) {
        Calendar cal = Calendar.getInstance();
        long timel = cal.getTimeInMillis() - time;
        if (timel / 1000 < 60) {
            return "1分钟以内";
        } else if (timel / 1000 / 60 < 60) {
            return timel / 1000 / 60 + "分钟前";
        } else if (timel / 1000 / 60 / 60 < 24) {
            return timel / 1000 / 60 / 60 + "小时前";
        } else {
            return timel / 1000 / 60 / 60 / 24 + "天前";
        }
    }

    /**
     * 指定时间距离当前时间信息   用于判断 时间
     *
     * @param time
     * @return
     */
    public static long getLnowLong(long time) {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis() - time;
    }
}
