package com.omi.config.databinding;

import android.databinding.BindingConversion;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SensYang on 2017/03/09 15:42
 */

public class TypeConversion {
    @BindingConversion
    public static String convertDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }

    @BindingConversion
    public static String convertDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }
}
