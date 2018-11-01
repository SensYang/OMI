package com.omi.net.easemob;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.omi.bean.chat.IMMessage;
import com.omi.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by SensYang on 2017/04/05 19:10
 */

public class SendMessageCallBackVO implements EMCallBack {
    private static List<SendMessageCallback> sendMessageCallbackList = new ArrayList<>();

    public static void addSendMessageCallback(SendMessageCallback callback) {
        sendMessageCallbackList.add(callback);
    }

    public static void removeSendMessageCallback(SendMessageCallback callback) {
        sendMessageCallbackList.remove(callback);
    }

    private IMMessage message;

    public SendMessageCallBackVO(EMMessage message) {
        if (sendMessageCallbackList.size() == 0) return;
        if (message.getMsgId() == null) {
            message.setMsgId(UUID.randomUUID().toString());
        }
        this.message = new IMMessage(message);
        for (SendMessageCallback sendMessageCallback : sendMessageCallbackList) {
            sendMessageCallback.onSend(this.message);
        }
    }

    @Override
    public void onSuccess() {
        Log.e("send--->" + "onSuccess");
        if (sendMessageCallbackList.size() == 0) return;
        for (SendMessageCallback sendMessageCallback : sendMessageCallbackList) {
            sendMessageCallback.onSuccess(message);
        }
    }

    @Override
    public void onError(int code, String error) {
        Log.e("send--->" + "onError" + code + "___" + error);
        if (sendMessageCallbackList.size() == 0) return;
        for (SendMessageCallback sendMessageCallback : sendMessageCallbackList) {
            sendMessageCallback.onError(message, code, error);
        }
    }

    @Override
    public void onProgress(int progress, String status) {
        Log.e("send--->" + "onProgress" + progress + "___" + status);
        if (message != null) message.setProgress(progress);
        if (sendMessageCallbackList.size() == 0) return;
        for (SendMessageCallback sendMessageCallback : sendMessageCallbackList) {
            sendMessageCallback.onProgress(message, progress, status);
        }
    }
}
