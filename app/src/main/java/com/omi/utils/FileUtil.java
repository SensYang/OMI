package com.omi.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.omi.OmiApplication;

import java.io.File;

/**
 * Created by SensYang on 2017/04/06 20:54
 */

public class FileUtil {
    public static String getPathWithUri(Uri uri) {
        if (uri == null) return null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = OmiApplication.getInstance().getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                return null;
            }
            return picturePath;
        } else {
            File file = new File(uri.getPath());
            if (!file.exists()) {
                return null;
            }
            return file.getAbsolutePath();
        }
    }
}
