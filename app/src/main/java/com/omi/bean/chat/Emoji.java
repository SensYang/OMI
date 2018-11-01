package com.omi.bean.chat;

/**
 * Created by SensYang on 2017/03/22 18:28
 */

public class Emoji {
    private int imgRes;
    private String emojiString;

    public Emoji(int imgRes, String emojiString) {
        this.imgRes = imgRes;
        this.emojiString = emojiString;
    }

    public int getImgRes() {
        return imgRes;
    }

    public String getEmojiString() {
        return emojiString;
    }
}