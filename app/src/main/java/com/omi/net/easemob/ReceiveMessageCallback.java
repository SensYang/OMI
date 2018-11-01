package com.omi.net.easemob;

import com.omi.bean.chat.IMMessage;

import java.util.List;

/**
 * Created by SensYang on 2017/04/06 21:11
 */

public interface ReceiveMessageCallback {
    void onMessageReceived(List<IMMessage> messages);

    void onCmdMessageReceived(List<IMMessage> messages);

    void onMessageRead(List<String> messageIds);

    void onMessageDelivered(List<String> messageIds);

    void onMessageChanged(String messageId, Object change);
}
