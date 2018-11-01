package com.omi;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by SensYang on 2017/04/02 21:12
 */

public class BaseTest {
    @Test
    public void stringTest() {
//        String s = "a123456";
//        System.out.println(MD5.encode(s));
//        System.out.println(get32MD5Str(s));
//        System.out.println("\u6ce8\u518c\u65b0\u7535\u8bdd\u7cfb\u7edf\u5931\u8d25");
        System.out.println("\u5904\u7406\u6210\u529f\uff01");
    }
    public final static String get32MD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }
}
