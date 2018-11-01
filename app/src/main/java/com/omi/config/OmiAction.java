package com.omi.config;

import android.net.Uri;

/**
 * Created by SensYang on 2017/03/27 19:00
 */

public class OmiAction {
    public static final int ACTION_SELECT_LOCATION = 0x10;
    public static final int ACTION_PICK_PICTURE = 0x11;
    public static final int ACTION_TAKE_PHOTO = 0x12;
    public static final int ACTION_CROP = 0x13;
    public static final int ACTION_PICK = 0x14;
    public static final int ACTION_LOCATION = 0x15;
    public static final int ACTION_RECORD_VIDEO = 0x16;
    public static final int ACTION_GROUP_INFO = 0x17;
    public static final int RESULT_EXIT_GROUP = 0x18;
    public static final int ACTION_PUBLISH_DYNAMIC = 0x19;
    public static Uri TEMP_URI;
}