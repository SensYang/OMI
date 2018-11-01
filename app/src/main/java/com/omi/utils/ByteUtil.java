package com.omi.utils;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SensYang on 2017/04/19 13:49
 */

public class ByteUtil {
    private static String hexStr = "0123456789abcdef";

    public static String byteToString16(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String fileToHexString(File file) {
        if (file == null || file.isDirectory() || !file.exists()) return null;
        InputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            bis = new BufferedInputStream(fis, fis.available());
            bis.read(buffer);
            return byteToString16(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String fileToBase64(File file) {
        if (file == null || file.isDirectory() || !file.exists()) return null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
