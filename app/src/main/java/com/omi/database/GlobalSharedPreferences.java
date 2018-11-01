package com.omi.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.omi.OmiApplication;

/**
 * Created by SensYang on 2016/03/31
 */
public class GlobalSharedPreferences {
    private static GlobalSharedPreferences globalSharedPreferences;
    /**
     * 参数配置信息
     */
    private static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    /**
     * filename
     */
    public static final String SHAREDPREFERENCES_GOLBAL = "omi_global";//标记用户的公共属性

    private GlobalSharedPreferences() {
    }

    public static GlobalSharedPreferences getInstance() {
        if (globalSharedPreferences == null) {
            globalSharedPreferences = new GlobalSharedPreferences();
            preferences = OmiApplication.getInstance().getSharedPreferences(SHAREDPREFERENCES_GOLBAL, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return globalSharedPreferences;
    }

    /**
     * 判断配置文件是否含有key值
     */
    public boolean isContainsKey(String key) {
        return preferences == null ? false : preferences.contains(key);
    }

    /**
     * 向配置文件存入boolean类型的键值对key——>value
     */
    public void putBoolean(String key, boolean value) {
        if (editor != null) {
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件boolean类型的键值对key——>defValue
     */
    public boolean getBoolean(String key, boolean defValue) {
        return preferences == null ? null : preferences.getBoolean(key, defValue);
    }

    /**
     * 向配置文件添加String类型的键值对key——>value
     */
    public void putString(String key, String value) {
        if (editor != null) {
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件String类型的键值对key——>defValue
     */
    public String getString(String key, String defValue) {
        return preferences == null ? null : preferences.getString(key, defValue);
    }

    /**
     * 向配置文件添加int类型的键值对key——>value
     */
    public void putInt(String key, int value) {
        if (editor != null) {
            editor.putInt(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件int类型的键值对key——>defValue
     */
    public int getInt(String key, int defValue) {
        return preferences == null ? defValue : preferences.getInt(key, defValue);
    }

    /**
     * 向配置文件添加long类型的键值对key——>value
     */
    public void putLong(String key, long value) {
        if (editor != null) {
            editor.putLong(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件long类型的键值对key——>defValue
     */
    public long getLong(String key, long defValue) {
        return preferences == null ? defValue : preferences.getLong(key, defValue);
    }

    /**
     * 删除信息
     */
    public void remove(String key) {
        if (editor != null) {
            editor.remove(key);
            editor.commit();
        }
    }
}
