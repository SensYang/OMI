package com.omi.net.easemob;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.kincai.libjpeg.ImageUtils;
import com.omi.utils.ContactsUtil;
import com.omi.utils.FileUtil;
import com.omi.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SensYang on 2017/03/16 14:11
 */

public class ApiByEasemob {
    private static ApiByEasemob instance = new ApiByEasemob();

    public static ApiByEasemob getInstance() {
        return instance;
    }

    private ApiByEasemob() {
    }

    /**
     * 发送文本消息
     *
     * @param content        发送的信息
     * @param toChatUsername 发给谁
     */
    public void sendTxtMessage(String content, String toChatUsername) {
        if (content == null) return;
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        if (!ContactsUtil.isMobileNO(toChatUsername))
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    //    /**
    //     * 发送语音消息
    //     * 扩展自文本消息
    //     *
    //     * @param voiceContent   语音文本
    //     * @param length
    //     * @param toChatUsername
    //     */
    //    public void sendVoiceMessage(String voiceContent, long length, String toChatUsername) {
    //        if (voiceContent == null) return;
    //        EMMessage message = EMMessage.createTxtSendMessage(voiceContent, toChatUsername);
    //        message.setAttribute("isVoice", true);
    //        message.setAttribute("mediaLength", length);
    //        if (!ContactsUtil.isMobileNO(toChatUsername))
    //            message.setChatType(EMMessage.ChatType.GroupChat);
    //        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
    //        EMClient.getInstance().chatManager().sendMessage(message);
    //    }

    /**
     * 发送语音消息
     * 扩展自文本消息
     *
     * @param filePath       语音路径
     * @param timeLength
     * @param toChatUsername
     */
    public void sendVoiceMessage(String filePath, int timeLength, String toChatUsername) {
        if (filePath == null || timeLength == 401) return;
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, timeLength, toChatUsername);
        if (!ContactsUtil.isMobileNO(toChatUsername))
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送视频消息
     */
    public void sendVideoMessage(Uri videoUri, String toChatUsername) {
        if (videoUri == null || toChatUsername == null || toChatUsername.length() == 0) return;
        new SendVideoThread(videoUri, toChatUsername).start();
    }

    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");

    class SendVideoThread extends Thread {
        String videoPath;
        String toChatUsername;

        public SendVideoThread(Uri videoUri, String toChatUsername) {
            this.videoPath = FileUtil.getPathWithUri(videoUri);
            this.toChatUsername = toChatUsername;
        }

        @Override
        public void run() {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            Bitmap bitmap = mmr.getFrameAtTime();
            String outPath = FileUtils.makeImagePath(format.format(new Date()));
            ImageUtils.compressBitmap(bitmap, outPath);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            int videoLength = (int) (Long.parseLong(duration) / 1000);
            EMMessage message = EMMessage.createVideoSendMessage(videoPath, outPath, videoLength, toChatUsername);
            if (!ContactsUtil.isMobileNO(toChatUsername))
                message.setChatType(EMMessage.ChatType.GroupChat);
            message.setMessageStatusCallback(new SendMessageCallBackVO(message));
            EMClient.getInstance().chatManager().sendMessage(message);
        }
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(Uri imagePath, String toChatUsername) {
        if (imagePath == null) return;
        sendImageMessage(FileUtil.getPathWithUri(imagePath), toChatUsername);
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(String imagePath, String toChatUsername) {
        if (imagePath == null) return;
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        if (!ContactsUtil.isMobileNO(toChatUsername))
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送位置消息
     */
    public void sendLocationMessage(double latitude, double longitude, String locationAddress, String toChatUsername) {
        if (locationAddress == null) return;
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        if (!ContactsUtil.isMobileNO(toChatUsername))
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送文件消息
     */
    public void sendFileMessage(Uri filePath, String toChatUsername) {
        if (filePath == null) return;
        sendFileMessage(FileUtil.getPathWithUri(filePath), toChatUsername);
    }

    /**
     * 发送文件消息
     */
    public void sendFileMessage(String filePath, String toChatUsername) {
        if (filePath == null) return;
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        if (!ContactsUtil.isMobileNO(toChatUsername))
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送透传信息
     */
    public void sendCMDMessage(String action, String toUsername) {
        if (action == null) return;
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.CMD);
        if (!ContactsUtil.isMobileNO(toUsername)) message.setChatType(EMMessage.ChatType.GroupChat);
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        message.setTo(toUsername);
        message.addBody(cmdBody);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送大表情
     *
     * @param content 大表情的gif网络地址
     */
    public void sendGifMessage(String content, String toChatUsername) {
        if (content == null) return;
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        // 增加自己特定的属性
        message.setAttribute("isGif", true);
        message.setMessageStatusCallback(new SendMessageCallBackVO(message));
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}