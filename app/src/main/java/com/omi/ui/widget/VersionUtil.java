package com.omi.ui.widget;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.omi.OmiApplication;

/**
 * Created by SensYang on 2016/8/17 0017.
 */
public class VersionUtil {
    /**
     * 获取软件版本号
     */
    public static int getApplicationVersion() {
        try {
            return OmiApplication.getInstance().getPackageManager().getPackageInfo(OmiApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取软件版本号
     */
    public static String getApplicationVersionName() {
        try {
            return OmiApplication.getInstance().getPackageManager().getPackageInfo(OmiApplication.getInstance().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取软件版本号
     */
    public static int getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName() {
        return OmiApplication.getInstance().getPackageName();
    }

    /**
     * 获取应用名
     */
    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = OmiApplication.getInstance().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(OmiApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }
}
