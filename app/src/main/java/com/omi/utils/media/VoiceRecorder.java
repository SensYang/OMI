package com.omi.utils.media;

import android.media.MediaRecorder;
import android.os.SystemClock;
import android.text.format.Time;

import com.hyphenate.chat.EMClient;
import com.hyphenate.util.PathUtil;
import com.omi.utils.Log;

import java.io.File;
import java.io.IOException;

public class VoiceRecorder {
    private MediaRecorder recorder;
    private static final String EXTENSION = ".amr";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;

    /**
     * start recording to the file
     */
    public String startRecording() {
        try {
            file = null;
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1);
            recorder.setAudioSamplingRate(8000);
            recorder.setAudioEncodingBitRate(64);
            voiceFileName = getVoiceFileName(EMClient.getInstance().getCurrentUser());
            voiceFilePath = PathUtil.getInstance().getVoicePath() + "/" + voiceFileName;
            file = new File(voiceFilePath);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isRecording) {
                            if (onAmplitudeListener != null) {
                                onAmplitudeListener.onAmplitude(recorder.getMaxAmplitude() * 100 / 0x7FFF);
                            }
                            //TODO
                            SystemClock.sleep(100);
                        }
                    } catch (Exception e) {
                        Log.e("voice", e.toString());
                    }
                }
            }).start();
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            Log.e("voice", "prepare() failed");
        }
        Log.d("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */
    public void cancelRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            if (file != null && file.exists() && !file.isDirectory()) {
                file.delete();
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            if (file == null || !file.exists() || !file.isFile()) {
                return -1;
            }
            if (file.length() == 0) {
                file.delete();
                return -1;
            }
            int seconds = (int) (System.currentTimeMillis() - startTime) / 1000;
            Log.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }

    private OnAmplitudeListener onAmplitudeListener;

    public void setOnAmplitudeListener(OnAmplitudeListener onAmplitudeListener) {
        this.onAmplitudeListener = onAmplitudeListener;
    }

    public interface OnAmplitudeListener {
        void onAmplitude(int amplitude);
    }
}
