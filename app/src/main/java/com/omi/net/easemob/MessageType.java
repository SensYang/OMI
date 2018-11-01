package com.omi.net.easemob;

import com.hyphenate.chat.EMMessage;

/**
 * Created by SensYang on 2017/04/05 20:03
 */

public enum MessageType {
    TXT, IMAGE, VIDEO, LOCATION, VOICE, FILE, CMD, GIF;

    public static MessageType typeFromMessage(EMMessage message) {
        int typeAt = message.getType().ordinal();
        if (typeAt > 0 && typeAt != 4) {
            return MessageType.values()[typeAt];
        } else {
            if (message.getBooleanAttribute("isVoice", false)) {
                return VOICE;
            } else if (message.getBooleanAttribute("isGif", false)) {
                return GIF;
            }
        }
        return TXT;
    }

}
