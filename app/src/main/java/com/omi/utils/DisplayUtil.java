package com.omi.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.omi.OmiApplication;

import java.lang.reflect.Field;

public class DisplayUtil {

    private static DisplayMetrics displaysMetrics = null;
    private static float scale = -1.0f;

    public static DisplayMetrics getDisplayMetrics() {
        if (null == displaysMetrics) {
            displaysMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) OmiApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(displaysMetrics);
        }
        return displaysMetrics;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight() {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        return displaysMetrics.heightPixels;
    }

    private static int getStatusBarHeight(Resources resources) {
        Class<?> c;
        Object obj;
        Field field;
        int x, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = resources.getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    private static int statusBarHeight = 0;

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        if (statusBarHeight == 0 && OmiApplication.getInstance() != null) {
            statusBarHeight = getStatusBarHeight(OmiApplication.getInstance().getResources());
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWidth() {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        return displaysMetrics.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(float dpValue) {
        if (scale < 0) {
            if (null == displaysMetrics) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }
        return dpValue * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(float pxValue) {
        if (scale < 0) {
            if (null == displaysMetrics) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }
        return pxValue / scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从sp 的单位 转成为 dp
     */
    public static float sp2px(float spValue) {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        float fontScale = displaysMetrics.scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
