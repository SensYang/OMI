package com.omi.database;

import com.omi.utils.DisplayUtil;

/**
 * Created by SensYang on 2016/4/9 0009.
 */
public enum PreferencesSetting {
    /**
     * 是否引导过
     */
    HAS_GUIDE("has_guide", false), /**
     * 登录过的账号
     */
    LOGINED_PHONE("logined_phone", null), /**
     * 登录过的id
     */
    LOGINED_MEMBER_ID("logined_member_id", null), /**
     * 登陆过的密码
     */
    LOGINED_PASS("logined_pass", null), /**
     * 键盘高度
     */
    SOFT_KEYBOARD_HEIGHT("softkeyboardheight", (int) DisplayUtil.dip2px(280));
    private final String settingName;
    private final Object defaultValue;

    PreferencesSetting(String settingName, Object defaultValue) {
        this.settingName = settingName;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取字段名
     */
    public String getName() {
        return settingName;
    }

    /**
     * 获取默认值
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * 根据字段名字实例化
     */
    public static PreferencesSetting fromSetting(String settingName) {
        PreferencesSetting[] values = values();
        for (PreferencesSetting value : values) {
            if (value.settingName.equalsIgnoreCase(settingName)) {
                return value;
            }
        }
        return null;
    }
}
