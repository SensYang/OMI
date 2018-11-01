package com.omi.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.omi.OmiApplication;
import com.omi.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SensYang on 2016/6/30 0030.
 */
public class FileUtils {
    public static final String DIR_PICTURES = "pictures";

    /**
     * 创建目录
     */
    public static File makeDirs(String dirName) {
        if (!hasSdcard()) {
            ToastUtil.showToast(R.string.no_sd);
            return null;
        }
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + VersionUtil.getApplicationName() + File.separator + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static Uri makeImageUri(String fileName) {
        File dir = FileUtils.makeDirs(FileUtils.DIR_PICTURES);
        if (dir != null) return Uri.fromFile(new File(dir, fileName + ".jpg"));
        else return null;
    }

    public static String makeImagePath(String fileName) {
        File dir = FileUtils.makeDirs(FileUtils.DIR_PICTURES);
        return new File(dir, fileName + ".jpg").getPath();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        // 有存储的SDCard
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Uri转绝对路径
     */
    public static String getRealFilePath(Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = OmiApplication.getInstance().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 读取Assets的文件
     */
    public static String getStringFromAssetsFile(String fileName) {
        try {
            InputStream in = OmiApplication.getInstance().getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            in.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return file.length();
        }
        return 0;
    }
}