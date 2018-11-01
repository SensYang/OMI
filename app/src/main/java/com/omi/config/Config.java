package com.omi.config;

import com.omi.OmiApplication;
import com.omi.R;

/**
 * Created by Calvin on 2015/6/18 0018.
 */
public class Config {
    /**
     * 是否开启调试模式
     */
    public static boolean ISDEBUG = false;
    /**
     * 是否开启耳机模式
     */
    public static boolean isEarphoneMode = false;
    private static int main_color = -1;

    public static int getMain_color() {
        if (main_color == -1 && OmiApplication.getInstance() != null)
            main_color = OmiApplication.getInstance().getResources().getColor(R.color.main_color);
        return main_color;
    }

}