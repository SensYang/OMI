package com.omi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by liuyang on 2016/3/30.
 */
public class PermissionUtil {
    public enum PermissionCode {
        /**
         * 蓝牙权限
         */
        BLUETOOTH_PERMISSION(0x10, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
        /**
         * 摄像头权限
         */
        CAMERA_PERMISSION(0x11, Manifest.permission.CAMERA),
        /**
         * 定位权限
         */
        LOCATION_PERMISSION(0x12, Manifest.permission.ACCESS_COARSE_LOCATION);

        int requestCode;
        String[] permissions;

        PermissionCode(int requestCode, String... permissions) {
            this.requestCode = requestCode;
            this.permissions = permissions;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public String[] getPermissions() {
            return permissions;
        }

        public static PermissionCode parseOf(int requestCode) {
            switch (requestCode) {
                case 0x10:
                    return BLUETOOTH_PERMISSION;
                case 0x11:
                    return CAMERA_PERMISSION;
                case 0x12:
                    return LOCATION_PERMISSION;
            }
            return null;
        }

        public static PermissionCode parseOf(String permission) {
            switch (permission) {
                case Manifest.permission.BLUETOOTH:
                case Manifest.permission.BLUETOOTH_ADMIN:
                    return BLUETOOTH_PERMISSION;
                case Manifest.permission.CAMERA:
                    return CAMERA_PERMISSION;
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                    return LOCATION_PERMISSION;
            }
            return null;
        }

    }

    /**
     *   检查权限
     */
    public static boolean checkPermission(Activity activity, PermissionCode permissionCode) {
        if (VersionUtil.getAndroidVersion() < 23) {
            return checkLowerPermission(activity, permissionCode);
        } else {
            return checkHigherPermission(activity, permissionCode);
        }
    }

    private static boolean checkHigherPermission(Activity activity, PermissionCode permissionCode) {
        String[] permissions = permissionCode.getPermissions();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, permissionCode.getRequestCode());
                return false;
            }
        }
        return true;
    }

    private static boolean checkLowerPermission(Activity activity, PermissionCode permissionCode) {
        String[] permissions = permissionCode.getPermissions();
        PackageManager packageManager = activity.getPackageManager();
        for (String permission : permissions) {
            if (packageManager.checkPermission(permission, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.parse("package:" + activity.getPackageName());
                intent.setData(uri);
                activity.startActivity(intent);
                return false;
            }
        }
        return true;
    }
}