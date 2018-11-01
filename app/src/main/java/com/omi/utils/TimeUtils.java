package com.omi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SensYang on 2017/03/11 14:07
 */

public class TimeUtils {

    public static String showDate(long time) {
        return showDate(new Date(time));
    }

    public static String showDate(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String dateStr = format(date, pattern);
        String year = dateStr.substring(0, 4);
        Long yearNum = Long.parseLong(year);
        int month = Integer.parseInt(dateStr.substring(5, 7));
        int day = Integer.parseInt(dateStr.substring(8, 10));
        String hour = dateStr.substring(11, 13);
        String minute = dateStr.substring(14, 16);
        long addtime = date.getTime();
        long today = System.currentTimeMillis();//当前时间的毫秒数
        Date now = new Date(today);
        String nowStr = format(now, pattern);
        int nowDay = Integer.parseInt(nowStr.substring(8, 10));
        String result = "";
        long l = today - addtime;//当前时间与给定时间差的毫秒数
        long days = l / (24 * 60 * 60 * 1000);//这个时间相差的天数整数，大于1天为前天的时间了，小于24小时则为昨天和今天的时间
        long hours = (l / (60 * 60 * 1000) - days * 24);//这个时间相差的减去天数的小时数
        //        long min = ((l / (60 * 1000)) - days * 24 * 60 - hours * 60);//
        //        long s = (l / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - min * 60);
        if (days > 0) {
            if (days > 0 && days < 2) {
                result = "前天";
            } else {
                result = yearNum % 100 + "/" + month + "/" + day;
            }
        } else if (hours > 0 && day != nowDay) {
            result = "昨天";
        } else {
            result = hour + ":" + minute;
        }
        return result;
    }

    /**
     * 日期格式化
     *
     * @param time    需要格式化的时间
     * @param pattern 时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的时间字符串
     */
    public static String format(long time, String pattern) {
        return format(new Date(time), pattern);
    }

    /**
     * 日期格式化
     *
     * @param date    需要格式化的日期
     * @param pattern 时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的时间字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 格式化时间长度
     */
    public static String showDuration(long duration) {
        duration = duration / 1000;//s
        int second = (int) (duration % 60);//秒
        int minute = (int) ((duration - second) / 60);//分钟
        StringBuilder builder = new StringBuilder();
        if (minute > 0) {
            builder.append(minute + "'");
        }
        builder.append(second + "''");
        return builder.toString();
    }

    /**
     * 格式化时间长度
     */
    public static String formatDuration(long duration) {
        duration = duration / 1000;//s
        int second = (int) (duration % 60);//秒
        int minute = (int) ((duration - second) / 60);//分钟
        StringBuilder builder = new StringBuilder();
        if (minute > 0) {
            builder.append(minute + ":");
        } else {
            builder.append("00:");
        }
        if (second > 9) builder.append(second);
        else builder.append("0" + second);
        return builder.toString();
    }

}
