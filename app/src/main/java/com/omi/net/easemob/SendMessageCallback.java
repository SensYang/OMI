package com.omi.net.easemob;

import com.omi.bean.chat.IMMessage;

/**
 * Created by SensYang on 2017/04/05 19:11
 */
public interface SendMessageCallback {
    /**
     * 开始发送消息
     */
    void onSend(IMMessage message);

    /**
     * 发送成功
     */
    void onSuccess(IMMessage message);

    /**
     * 发送失败
     */
    void onError(IMMessage message, int code, String error);

    /**
     * 发送进度回调
     */
    void onProgress(IMMessage message, int progress, String status);
}