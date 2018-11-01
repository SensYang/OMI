package com.omi.net.easemob;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.omi.bean.chat.IMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/04/06 21:09
 */

public class ReceiveMessageCallbackVO implements EMMessageListener {
    private static ReceiveMessageCallbackVO instance = new ReceiveMessageCallbackVO();

    private ReceiveMessageCallbackVO() {}

    public static ReceiveMessageCallbackVO getInstance() {
        return instance;
    }

    private static List<ReceiveMessageCallback> receiveMessageCallbackList = new ArrayList<>();

    public static void addReceiveMessageCallback(ReceiveMessageCallback callback) {
        receiveMessageCallbackList.add(callback);
    }

    public static void removeReceiveMessageCallback(ReceiveMessageCallback callback) {
        receiveMessageCallbackList.remove(callback);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        if (receiveMessageCallbackList.size() == 0) return;
        List<IMMessage> list = new ArrayList<>(messages.size());
        for (EMMessage message : messages) {
            list.add(new IMMessage(message));
        }
        for (ReceiveMessageCallback receiveMessageCallback : receiveMessageCallbackList) {
            receiveMessageCallback.onMessageReceived(list);
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        if (receiveMessageCallbackList.size() == 0) return;
        List<IMMessage> list = new ArrayList<>(messages.size());
        for (EMMessage message : messages) {
            list.add(new IMMessage(message));
        }
        for (ReceiveMessageCallback receiveMessageCallback : receiveMessageCallbackList) {
            receiveMessageCallback.onCmdMessageReceived(list);
        }
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (receiveMessageCallbackList.size() == 0) return;
        List<String> list = new ArrayList<>(messages.size());
        for (EMMessage message : messages) {
            list.add(message.getMsgId());
        }
        for (ReceiveMessageCallback receiveMessageCallback : receiveMessageCallbackList) {
            receiveMessageCallback.onMessageRead(list);
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (receiveMessageCallbackList.size() == 0) return;
        List<String> list = new ArrayList<>(messages.size());
        for (EMMessage message : messages) {
            list.add(message.getMsgId());
        }
        for (ReceiveMessageCallback receiveMessageCallback : receiveMessageCallbackList) {
            receiveMessageCallback.onMessageDelivered(list);
        }
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        if (receiveMessageCallbackList.size() == 0) return;
        for (ReceiveMessageCallback receiveMessageCallback : receiveMessageCallbackList) {
            receiveMessageCallback.onMessageChanged(message.getMsgId(), change);
        }
    }
}
