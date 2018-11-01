package com.omi.config.databinding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by SensYang on 2017/04/05 16:29
 */

public class ViewAttrAdapter {
    @BindingAdapter("android:onTouchListener")
    public static void setOnTouchListener(View view, View.OnTouchListener l) {
        view.setOnTouchListener(l);
    }
}
