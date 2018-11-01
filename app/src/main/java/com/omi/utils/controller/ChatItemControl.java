package com.omi.utils.controller;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.omi.OmiApplication;
import com.omi.bean.chat.IMMessage;
import com.omi.config.Config;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.activity.utils.ImageBrowseActivity;
import com.omi.ui.activity.utils.amap.LocationSelectActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by SensYang on 2017/04/12 14:31
 */

public class ChatItemControl implements MediaPlayer.OnCompletionListener {
    private static ChatItemControl instance = new ChatItemControl();

    public static ChatItemControl getInstance() {
        return instance;
    }

    private AudioManager audioManager = (AudioManager) OmiApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
    private MediaPlayer mediaPlayer;

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    private void playUrl(String url) {
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void voiceClick(View view, IMMessage message) {
        message.setHasRead(true);
        String content = message.getContent();
        if (content == null) return;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stop();
            return;
        }
        stop();
        mediaPlayer = new MediaPlayer();
        if (Config.isEarphoneMode) {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        } else {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        }
        playUrl(content);
    }

    public void onVideoClick(View view, IMMessage message) {
        message.setHasRead(true);
        String localFilePath = message.getContent();
        if (localFilePath != null && new File(localFilePath).exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(localFilePath)), "video/mp4");
            view.getContext().startActivity(intent);
        } else {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom());
            if (conversation != null) {
                EMMessage emMessage = conversation.getMessage(message.getMessageId(), true);
                if (emMessage != null) {
                    emMessage.setMessageStatusCallback(new MediaDownloadListener(view.getContext(), message));
                    EMClient.getInstance().chatManager().downloadAttachment(emMessage);
                }
            }
        }
    }

    private class MediaDownloadListener implements EMCallBack {
        private IMMessage message;
        private Context context;

        public MediaDownloadListener(Context context, IMMessage message) {
            message.setHasRead(true);
            this.message = message;
            this.context = context;
        }

        @Override
        public void onSuccess() {
            String localFilePath = message.getContent();
            if (localFilePath != null && new File(localFilePath).exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(localFilePath)), "video/mp4");
                context.startActivity(intent);
            }
        }

        @Override
        public void onProgress(int progress, String status) {
            message.setProgress(progress);
        }

        @Override
        public void onError(int code, String error) {
            File file = new File(message.getContent());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void onImageClick(View view, IMMessage message) {
        message.setHasRead(true);
        Intent intent = new Intent(view.getContext(), ImageBrowseActivity.class);
        String url = null;
        if (message.getContent() != null) url = message.getContent();
        else url = message.getThumbnailUrl();
        intent.putExtra("imageurls", new String[]{url});
        view.getContext().startActivity(intent);
    }

    public void onLocationClick(View view, IMMessage message) {
        message.setHasRead(true);
        Intent intent = new Intent(view.getContext(), LocationSelectActivity.class);
        intent.putExtra("latitude", message.getLatitude());
        intent.putExtra("longitude", message.getLongitude());
        view.getContext().startActivity(intent);
    }

    public void onHeadClick(View view, String userid) {
        if (userid == null) return;
        if (userid.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
            Intent intent = new Intent(view.getContext(), SelfInfoActivity.class);
            view.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
            intent.putExtra("userid", userid);
            view.getContext().startActivity(intent);
        }
    }

}
