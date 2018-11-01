package com.omi.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;

import com.omi.database.GlobalSharedPreferences;
import com.omi.database.PreferencesSetting;

/**
 * Created by SensYang on 2016/4/16 0016.
 */
public class ResizeListenerLayout extends LinearLayout {

    public ResizeListenerLayout(Context context) {
        this(context, null);
    }

    public ResizeListenerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizeListenerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFitsSystemWindows(true);
    }

    private int chatControlHeight = 0;
    private View inputView;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (touchOutListener != null) {
            if (chatControlHeight == 0 && inputView != null) {
                chatControlHeight = inputView.getHeight();
            }
            if (ev.getY() < getHeight() - softKeyBoardHeight - chatControlHeight) {
                touchOutListener.onTouchOut();
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), 0, insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    private int softKeyBoardHeight = GlobalSharedPreferences.getInstance().getInt(PreferencesSetting.SOFT_KEYBOARD_HEIGHT.getName(), (int) PreferencesSetting.SOFT_KEYBOARD_HEIGHT.getDefaultValue());

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (keyBoardListener != null) if (insets.bottom != 0) {
            if (softKeyBoardHeight != insets.bottom) {
                softKeyBoardHeight = insets.bottom;
                GlobalSharedPreferences.getInstance().putInt(PreferencesSetting.SOFT_KEYBOARD_HEIGHT.getName(), softKeyBoardHeight);
            }
            keyBoardListener.onSoftKeyBoardStateChange(true, softKeyBoardHeight);
        } else {
            keyBoardListener.onSoftKeyBoardStateChange(false, softKeyBoardHeight);
        }
        insets.top = 0;
        return super.fitSystemWindows(insets);
    }

    private OnSoftKeyBoardListener keyBoardListener;

    public void setKeyBoardListener(OnSoftKeyBoardListener keyBoardListener) {
        this.keyBoardListener = keyBoardListener;
        if (this.keyBoardListener != null)
            this.keyBoardListener.onSoftKeyBoardStateChange(false, softKeyBoardHeight);
    }

    public interface OnSoftKeyBoardListener {
        void onSoftKeyBoardStateChange(boolean isShowing, int softKeyBoardHeight);
    }

    private OnTouchOutListener touchOutListener;

    public void setTouchOutListener(OnTouchOutListener touchOutListener) {
        this.touchOutListener = touchOutListener;
    }

    public interface OnTouchOutListener {
        void onTouchOut();
    }
}