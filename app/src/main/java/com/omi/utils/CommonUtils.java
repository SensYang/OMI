package com.omi.utils;

/**
 * @author xly 判断按钮是否被双击的类
 */
public class CommonUtils {
    private static long lastClick;

    /**
     * 两次点击时间间隔小于指定毫秒
     *
     * @param timeLong 指定毫秒
     */
    public static boolean isFastDoubleClick(long timeLong) {
        long time = System.currentTimeMillis();
        long timeBetween = time - lastClick;
        if (0 < timeBetween && timeBetween < timeLong) {    //两次点击时间小于指定毫秒
            return true;
        }
        lastClick = time;
        return false;
    }

    /**
     * 两次点击时间间隔小于400毫秒
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeBetween = time - lastClick;
        if (0 < timeBetween && timeBetween < 300) {    //两次点击时间小于300毫秒
            return true;
        }
        lastClick = time;
        return false;
    }

    /**
     * 两次点击时间小于10秒
     */
    public static boolean isFastPublishClick() {
        long time = System.currentTimeMillis();
        long timeBetween = time - lastClick;
        if (0 < timeBetween && timeBetween < 10000) {    //两次点击时间小于10秒
            return true;
        }
        lastClick = time;
        return false;
    }
}
