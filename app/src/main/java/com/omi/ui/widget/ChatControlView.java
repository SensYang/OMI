package com.omi.ui.widget;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omi.BR;
import com.omi.R;
import com.omi.bean.chat.Emoji;
import com.omi.bean.conversation.ConversationType;
import com.omi.databinding.WidgetChatControlViewBinding;
import com.omi.net.easemob.ApiByEasemob;
import com.omi.ui.widget.chatbroad.EmojiUtil;
import com.omi.utils.IMEUtil;
import com.omi.utils.TimeUtils;
import com.omi.utils.ToastUtil;
import com.omi.utils.media.VoiceRecorder;

/**
 * Created by SensYang on 2017/03/18 16:23
 */

public class ChatControlView extends LinearLayout implements Observable, ResizeListenerLayout.OnSoftKeyBoardListener, ResizeListenerLayout.OnTouchOutListener {
    private WidgetChatControlViewBinding chatControlViewBinding;
    private transient PropertyChangeRegistry mCallbacks;
    private String chatWith;
    private ImageView voiceIV;
    private TextView timeTV;
    private RecyclerView.LayoutManager layoutManager;
    private VoiceRecorder recorder;

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }

    public ChatControlView(Context context) {
        this(context, null);
    }

    public ChatControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        chatControlViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_chat_control_view, this, true);
        chatControlViewBinding.setChatControlView(this);
        chatControlViewBinding.contentET.addTextChangedListener(watcher);
        recorder = new VoiceRecorder();
    }

    private SimpleTextWatcher watcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (s.length() > 0) {
                setCanSendText(true);
            } else {
                setCanSendText(false);
            }
        }
    };

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public void setConversationType(ConversationType conversationType) {
        chatControlViewBinding.setConversationType(conversationType);
    }
    /////////////////////////////////////////////////////////////////
    /**
     * 当文本内容不为空即可发送
     */
    private boolean canSendText = false;
    /**
     * 文本内容
     */
    private SpannableString content;
    /**
     * 是否是语音状态
     */
    private boolean isVoiceState = false;
    /**
     * 是否是表情状态
     */
    private boolean isFaceState = false;
    /**
     * 是否是更多状态
     */
    private boolean isMoreState = false;
    /**
     * 是否取消语音发送
     */
    private boolean isVoiceCancle = true;
    /**
     * 是否是语音按钮按下
     */
    private boolean isVoicePress = false;
    /**
     * 是否需要重新开启更多面板
     */
    private int lastBoardState = -1;

    @Bindable
    public boolean isVoiceState() {
        return isVoiceState;
    }

    public void setVoiceState(boolean voiceState) {
        if (voiceState == isVoiceState) return;
        isVoiceState = voiceState;
        if (isVoiceState) {
            setFaceState(false);
            setMoreState(false);
        } else {
            chatControlViewBinding.contentET.setVisibility(VISIBLE);
        }
        scrollToBottom();
        notifyPropertyChanged(BR.voiceState);
    }

    private void scrollToBottom() {
        int position = layoutManager.getItemCount() - 1;
        if (position > 0) layoutManager.scrollToPosition(position);
    }

    @Bindable
    public boolean isFaceState() {
        return isFaceState;
    }

    public void setFaceState(boolean faceState) {
        if (faceState == isFaceState) return;
        isFaceState = faceState;
        if (isFaceState) {
            setVoiceState(false);
            setMoreState(false);
        }
        scrollToBottom();
        notifyPropertyChanged(BR.faceState);
    }

    @Bindable
    public boolean isMoreState() {
        return isMoreState;
    }

    public void setMoreState(boolean moreState) {
        if (moreState == isMoreState) return;
        isMoreState = moreState;
        if (isMoreState) {
            setFaceState(false);
            setVoiceState(false);
            scrollToBottom();
        }
        scrollToBottom();
        notifyPropertyChanged(BR.moreState);
    }

    @Bindable
    public CharSequence getContent() {
        return content;
    }

    public void setContent(SpannableString content) {
        if (this.content.equals(content)) return;
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public boolean isCanSendText() {
        return canSendText;
    }

    public void setCanSendText(boolean canSendText) {
        if (this.canSendText == canSendText) return;
        this.canSendText = canSendText;
        notifyPropertyChanged(BR.canSendText);
    }

    /**
     * 点击语音
     */
    public void onVoiceClick(View view) {
        chatControlViewBinding.chatExtensionBroadView.setVisibility(GONE);
        lastBoardState = -1;
        setVoiceState(!isVoiceState);
        if (isVoiceState) {
            IMEUtil.getInstance().hideSoftKeyboard(chatControlViewBinding.contentET);
            chatControlViewBinding.contentET.setVisibility(GONE);
        } else {
            chatControlViewBinding.contentET.setVisibility(VISIBLE);
            IMEUtil.getInstance().showSoftKeyboard(chatControlViewBinding.contentET);
        }
    }

    /**
     * 点击表情
     */
    public void onFaceClick(View view) {
        lastBoardState = 0;
        setFaceState(!isFaceState);
        if (isFaceState) {
            if (!softKeyBoardShowing)
                chatControlViewBinding.chatExtensionBroadView.setVisibility(VISIBLE);
            IMEUtil.getInstance().hideSoftKeyboard(chatControlViewBinding.contentET);
        } else {
            IMEUtil.getInstance().showSoftKeyboard(chatControlViewBinding.contentET);
        }
    }

    /**
     * 点击发送
     */
    public void onSendClick(View view) {
        ApiByEasemob.getInstance().sendTxtMessage(chatControlViewBinding.contentET.getText().toString(), chatWith);
        chatControlViewBinding.contentET.setText("");
    }

    /**
     * 点击更多
     */
    public void onMoreClick(View view) {
        lastBoardState = 1;
        setMoreState(!isMoreState);
        if (isMoreState) {
            if (!softKeyBoardShowing)
                chatControlViewBinding.chatExtensionBroadView.setVisibility(VISIBLE);
            IMEUtil.getInstance().hideSoftKeyboard(chatControlViewBinding.contentET);
            chatControlViewBinding.contentET.clearFocus();
        } else {
            IMEUtil.getInstance().showSoftKeyboard(chatControlViewBinding.contentET);
        }
    }

    /**
     * 点击emoji
     */
    public void onEmojiClick(Emoji emoji) {
        SpannableStringBuilder emojiString = EmojiUtil.stringToEmoji(chatControlViewBinding.contentET.getTextSize(), emoji.getEmojiString());
        int index = chatControlViewBinding.contentET.getSelectionStart();
        Editable editable = chatControlViewBinding.contentET.getText();
        editable.insert(index, emojiString);
    }

    private String voiceFilePath;

    /**
     * 发送语音
     */
    public void sendVoice() {
        int recordTime = recorder.stopRecoding();
        if (voiceFilePath == null || recordTime <= 1) {
            ToastUtil.showToast(R.string.short_time);
            return;
        }
        ApiByEasemob.getInstance().sendVoiceMessage(voiceFilePath, recordTime, chatWith);
    }

    /**
     * 取消语音发送
     * 删除语音取消发送
     */
    public void cancleVoice() {
        recorder.cancelRecording();
    }

    public void setVoiceIV(ImageView voiceIV) {
        this.voiceIV = voiceIV;
    }

    public void setTimeTV(TextView timeTV) {
        this.timeTV = timeTV;
    }

    @Bindable
    public boolean isVoiceCancle() {
        return isVoiceCancle;
    }

    public void setVoiceCancle(boolean voiceCancle) {
        if (voiceCancle == isVoiceCancle) return;
        isVoiceCancle = voiceCancle;
        notifyPropertyChanged(BR.voiceCancle);
        if (voiceCancle) {
            voiceIV.setImageResource(R.drawable.voicecancle);
        } else {
            voiceIV.setImageResource(R.drawable.voiceing);
        }
    }

    @Bindable
    public boolean isVoicePress() {
        return isVoicePress;
    }

    private long startTime;

    public void setVoicePress(boolean voicePress) {
        if (voicePress == isVoicePress) return;
        isVoicePress = voicePress;
        notifyPropertyChanged(BR.voicePress);
        if (voicePress) {
            voiceFilePath = recorder.startRecording();
            startTime = System.currentTimeMillis();
            handler.sendEmptyMessage(0);
            voiceIV.setVisibility(VISIBLE);
            timeTV.setVisibility(VISIBLE);
        } else {
            timeTV.setVisibility(GONE);
            voiceIV.setVisibility(GONE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            timeTV.setText(TimeUtils.formatDuration(System.currentTimeMillis() - startTime));
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private OnTouchListener voiceTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setVoiceCancle(false);
                    setVoicePress(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float X = event.getX();
                    float Y = event.getY();
                    if (X < 0 || X > v.getWidth() || Y < -3 * v.getHeight()) {
                        setVoiceCancle(true);
                    } else {
                        setVoiceCancle(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    setVoicePress(false);
                    if (isVoiceCancle) {
                        cancleVoice();
                    } else {
                        sendVoice();
                    }
                    break;
            }
            return true;
        }
    };

    public OnTouchListener getVoiceTouchListener() {
        return voiceTouchListener;
    }
    //////////////////////////////////////////////////////////////////////////////////
    /**
     * 更多面板的高度
     */
    private int moreBoardHeight = 0;

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * 重置更多面板的高度
     */
    @Bindable
    public int getMoreBoardHeight() {
        return moreBoardHeight;
    }

    public void setMoreBoardHeight(int moreBoardHeight) {
        if (this.moreBoardHeight == moreBoardHeight) return;
        this.moreBoardHeight = moreBoardHeight;
        notifyPropertyChanged(BR.moreBoardHeight);
    }

    private boolean softKeyBoardShowing = false;

    @Override
    public void onSoftKeyBoardStateChange(boolean isShowing, int softKeyBoardHeight) {
        softKeyBoardShowing = isShowing;
        if (softKeyBoardShowing) {
            scrollToBottom();
            chatControlViewBinding.chatExtensionBroadView.setVisibility(GONE);
            if (isVoiceState) {
                setVoiceState(false);
            }
            if (isFaceState) {
                setFaceState(false);
            }
            if (isMoreState) {
                setMoreState(false);
            }
        } else {
            if (lastBoardState > -1) {
                chatControlViewBinding.chatExtensionBroadView.setVisibility(VISIBLE);
                if (lastBoardState == 0) {
                    setFaceState(true);
                } else if (lastBoardState == 1) {
                    setMoreState(true);
                }
            }
        }
        setMoreBoardHeight(softKeyBoardHeight);
    }

    @Override
    public void onTouchOut() {
        lastBoardState = -1;
        chatControlViewBinding.chatExtensionBroadView.setVisibility(GONE);
        chatControlViewBinding.contentET.clearFocus();
        IMEUtil.getInstance().hideSoftKeyboard(chatControlViewBinding.contentET);
    }
}