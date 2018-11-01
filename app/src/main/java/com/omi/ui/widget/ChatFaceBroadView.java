package com.omi.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.omi.R;
import com.omi.databinding.WidgetChatFaceViewBinding;
import com.omi.ui.widget.chatbroad.EmojiUtil;
import com.omi.ui.widget.chatbroad.FaceAdapter;
import com.omi.utils.ToastUtil;


/**
 * Created by SensYang on 2017/03/18 16:27
 */

public class ChatFaceBroadView extends LinearLayout {
    private WidgetChatFaceViewBinding faceViewBinding;
    private FaceAdapter faceAdapter;
    private ChatControlView controlView;

    public ChatFaceBroadView(Context context) {
        this(context, null);
    }

    public ChatFaceBroadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatFaceBroadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        faceViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_chat_face_view, this, true);
        faceViewBinding.setChatFaceBroadView(this);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 7);
        faceViewBinding.setLayoutManager(layoutManager);
        faceAdapter = new FaceAdapter();
        faceAdapter.setControlView(controlView);
        initData();
        faceViewBinding.setFaceAdapter(faceAdapter);
    }

    public void setControlView(ChatControlView controlView) {
        this.controlView = controlView;
        if (faceAdapter != null)
            faceAdapter.setControlView(controlView);
    }

    private void initData() {
        faceAdapter.setEmojiList(EmojiUtil.getEmojiList());
    }

    public void onMoreFaceClick(View view) {
        ToastUtil.showToast("增加更多表情");
    }
}
