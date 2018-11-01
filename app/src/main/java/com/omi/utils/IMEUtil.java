package com.omi.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sens on 2015/2/6 0006.
 */
public class IMEUtil {
    private static IMEUtil instance;

    public static IMEUtil getInstance() {
        if (instance == null) instance = new IMEUtil();
        return instance;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard(View view) {
        view.requestFocus();
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}