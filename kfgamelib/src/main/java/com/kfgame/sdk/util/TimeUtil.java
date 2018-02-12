package com.kfgame.sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tobin on 2018/2/12.
 */

public class TimeUtil {

    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当前时间是否大于等于之前某个时间
     * @param nowTime
     * @param beginTime
     *
     */
    public static boolean afterTime(Date nowTime, Date beginTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        if (date.after(begin)) {
            return true;
        } else {
            return false;
        }
    }

    public static String pattern="yyyy-MM-dd";
    public static SimpleDateFormat formatter = new SimpleDateFormat(pattern);

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = formatter.parse(date1);
            mydate = formatter.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }
}
