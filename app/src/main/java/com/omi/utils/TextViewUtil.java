package com.omi.utils;

import android.text.Layout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by SensYang on 2017/03/10 17:58
 */

public class TextViewUtil {

    public static boolean checkOverLine(TextView textView) {
        int maxLine = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            maxLine = textView.getMaxLines();
        } else {
            try {
                maxLine = TextView.class.getDeclaredField("mMaximum").getInt(textView);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (maxLine == Integer.MAX_VALUE) return false;
        try {
            Field field = textView.getClass().getSuperclass().getDeclaredField("mLayout");
            field.setAccessible(true);
            Layout mLayout = (Layout) field.get(textView);
            if (mLayout == null)
                return false;
            return mLayout.getEllipsisCount(maxLine - 1) > 0 ? true : false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
}
