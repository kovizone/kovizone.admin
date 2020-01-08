package com.kovizone.admin.util;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    private static Map<String, SimpleDateFormat> simpleDateFormatMap = new HashMap<String, SimpleDateFormat>();

    /**
     * 日期格式:
     * 中线:
     * 年月日
     */
    public static final String YEAR_MONTH_DAY_ML = "yyyy-MM-dd";

    /**
     * 格式:
     * yyyyMMddHHmmssSS
     */
    public static final String YEAR_MONTH_DAY_MINUTES_MILLISECOND = "yyyyMMddHHmmssSS";


    /**
     * 两个日期想加
     *
     * @param date 年月日
     * @param time 时分秒
     * @return 组合成的
     */
    public static Date dateAddTime(Date date, Date time) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(time)) {
            return null;
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(time);

        calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
        calendarDate.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));

        return calendarDate.getTime();
    }

    /**
     * 比较两个日期的大小
     *
     * @param timeA
     * @param timeB
     * @return timeA大于timeB 返回true 反之:false
     */
    public static boolean compare(@NotNull Date timeA, @NotNull Date timeB) {
        if (timeA.getTime() - timeB.getTime() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 从静态Map中获取SimpleDateFormat，若不存在则新增。
     *
     * @param format
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sdf = null;
        try {
            sdf = simpleDateFormatMap.get(format);
        } catch (Exception e) {
            sdf = new SimpleDateFormat(format);
            simpleDateFormatMap.put(format, sdf);
        }
        if (sdf == null) {
            sdf = new SimpleDateFormat(format);
            simpleDateFormatMap.put(format, sdf);
        }
        return sdf;
    }

    /**
     * 格式化日期为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateFormat(Date date, String format) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
            return null;
        }
        return getSimpleDateFormat(format).format(date);
    }

    /**
     * 格式化字符串为日期
     *
     * @param date
     * @param format
     * @return
     */
    public static Date parseDate(String date, String format) {
        try {
            return getSimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 获取指定时间天数的最后一秒时间 即: 23:29:59
     *
     * @param date 时间
     * @return 修改后的时间
     */
    public static Date getDayEndSecond(Date date) {
        if (StringUtils.isEmpty(date)) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date parseDate(String arg) {
        if (arg == null) {
            return null;
        }
        arg = arg.trim();
        Date date = null;
        if (date == null) {
            date = parseDate(arg, "yyyy-MM-dd HH:mm:ss");
        }
        if (date == null) {
            date = parseDate(arg, "yyyy-MM-dd HH:mm:ss SSS");
        }
        if (date == null) {
            date = parseDate(arg, "yyyyMMddHHmmssSSS");
        }
        if (date == null) {
            date = parseDate(arg, "yyyyMMddHHmmss");
        }
        if (date == null) {
            date = parseDate(arg, "yyyy-MM-dd");
        }
        if (date == null) {
            date = parseDate(arg, "yy-MM-dd");
        }
        if (date == null) {
            date = parseDate(arg, "yyyyMMdd");
        }
        if (date == null) {
            date = parseDate(arg, "yyMMdd");
        }
        if (date == null) {
            date = parseDate(arg, "HH:mm:ss");
        }
        if (date == null) {
            date = parseDate(arg, "HHmmss");
        }

        if (date == null) {
            Set<String> keySet = simpleDateFormatMap.keySet();
            if (keySet != null) {
                for (String key : keySet) {
                    if (date == null) {
                        date = parseDate(arg, key);
                    }
                }
            }
        }
        return date;
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(YEAR_MONTH_DAY_ML);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time1 - time2) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    public static void main(String[] args) throws ParseException {
        // TODO Auto-generated method stub
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = sdf.parse("2019-10-08 10:10:10");
        Date d2 = sdf.parse("2019-11-15 00:00:00");
        System.out.println(daysBetween(d2, d1) + 1);
    }

}
