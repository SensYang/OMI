/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.omi.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.omi.OmiApplication;


/**
 * Toast工具类
 * Created by Jorstin on 2015/3/17.
 */
public class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;

    private static final Object synObj = new Object();

    /**
     * 显示一个文本
     */
    public static void showToast(CharSequence msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 资源文件方式显示文本
     */
    public static void showToast(int msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个文本
     */
    public static void showLongToast(CharSequence msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    /**
     * 资源文件方式显示文本
     */
    public static void showLongToast(int msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示一个文本并且设置时长
     */
    private static void showMessage(final CharSequence msg, final int len) {
        if (msg == null || msg.equals("")) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(OmiApplication.getInstance().getApplicationContext(), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     * 资源文件方式显示文本
     */
    private static void showMessage(final int msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(OmiApplication.getInstance().getApplicationContext(), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }
}