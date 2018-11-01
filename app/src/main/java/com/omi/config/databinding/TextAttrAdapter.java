package com.omi.config.databinding;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.omi.ui.widget.chatbroad.EmojiUtil;

/**
 * Created by SensYang on 2017/04/05 16:30
 */

public class TextAttrAdapter {
    @BindingAdapter("android:text")
    public static void setText(TextView textView, String string) {
        textView.setText(EmojiUtil.stringToEmoji(textView.getTextSize(), string));
    }
}