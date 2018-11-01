package com.omi.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.omi.bean.conversation.ConversationType;

/**
 * Created by SensYang on 2017/03/18 16:27
 */

public class ChatExtensionBroadView extends FrameLayout {
    private ChatFaceBroadView faceBroadView;
    private ChatMoreBroadView moreBroadView;
    private ChatControlView controlView;

    public ChatExtensionBroadView(Context context) {
        this(context, null);
    }

    public ChatExtensionBroadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatExtensionBroadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        faceBroadView = new ChatFaceBroadView(context);
        moreBroadView = new ChatMoreBroadView(context);
        faceBroadView.setControlView(controlView);
        //        lineView = new View(context);
        //        lineView.setBackgroundResource(R.color.line_color);
        //        lineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px((float) 0.5)));
    }

    public void setControlView(ChatControlView controlView) {
        this.controlView = controlView;
        if (faceBroadView != null) faceBroadView.setControlView(controlView);
    }

    public void setHeight(int height) {
        if (height == 0) return;
        if (getLayoutParams().height == height) return;
        getLayoutParams().height = height;
        setLayoutParams(getLayoutParams());
    }

    public void setShowFaceBroad(boolean showFaceBroad) {
        if (!showFaceBroad) return;
        removeAllViews();
        addView(faceBroadView);
    }

    public void setShowMoreBroad(boolean showMoreBroad) {
        if (!showMoreBroad) return;
        removeAllViews();
        addView(moreBroadView);
    }

    public void setConversationType(ConversationType conversationType) {
        moreBroadView.setConversationType(conversationType);
    }
}
