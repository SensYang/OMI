package com.omi.ui.widget.indexlist;

import net.sourceforge.pinyin4j.PinyinHelper;


/**
 * 字母工具类
 *
 * @Title:
 * @Description:
 * @Author:harlan
 * @Since:2014-5-8
 * @Version:
 */
public class LetterUtil {
    /**
     * @param chinese 一个汉字
     * @return 拼音首字母
     */
    public static String[] getFirstPinyin(char chinese) {
        return PinyinHelper.toHanyuPinyinStringArray(chinese);
    }

    /**
     * 是否是字母
     *
     * @return true 字母,false 非字母
     */
    public static boolean isLetter(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 112);
    }

    /**
     * 是否是汉字
     *
     * @return true 汉子,false 非汉字
     */
    public static boolean isChinese(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    /**
     * 取出第一个字母（大写）
     */
    public static char getFirstPinYinChar(char word) {
        if (isChinese(word)) {
            return getFirstPinyin(word)[0].toUpperCase().charAt(0);
        } else {
            return word;
        }
    }
}