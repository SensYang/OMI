package com.omi.bean.chat;

/**
 * Created by SensYang on 2017/04/04 15:29
 */

public enum IMMessageType {
    TXT_OTHER(0),
    TXT_SELF(100),
    IMAGE_OTHER(1),
    IMAGE_SELF(101),
    VIDEO_OTHER(2),
    VIDEO_SELF(102),
    LOCATION_OTHER(3),
    LOCATION_SELF(103),
    VOICE_OTHER(4),
    VOICE_SELF(104),
    FILE_OTHER(5),
    FILE_SELF(105),
    CMD_OTHER(6),
    CMD_SELF(106),
    GIF_OTHER(7),
    GIF_SELF(107),
    UNKNOW(404);
    private int value;

    IMMessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static IMMessageType valueOf(int type) {
        if(type>200)return UNKNOW;
        switch (type) {
            case 0: return TXT_OTHER;
            case 100: return TXT_SELF;
            case 1: return IMAGE_OTHER;
            case 101: return IMAGE_SELF;
            case 2: return VIDEO_OTHER;
            case 102: return VIDEO_SELF;
            case 3: return LOCATION_OTHER;
            case 103: return LOCATION_SELF;
            case 4: return VOICE_OTHER;
            case 104: return VOICE_SELF;
            case 5: return FILE_OTHER;
            case 105: return FILE_SELF;
            case 6: return CMD_OTHER;
            case 106: return CMD_SELF;
            case 7: return GIF_OTHER;
            case 107: return GIF_SELF;
            default:  return UNKNOW;
        }
    }
}
