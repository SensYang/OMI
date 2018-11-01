package com.omi.ui.widget.listener;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

/**
 * 重复点击事件
 * Created by SensYang on 2016/4/18 0018.
 * A class, that can be used as a TouchListener on any view (e.g. a Button).
 * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
 * click is fired immediately, next before initialInterval, and subsequent before
 * normalInterval.
 * <p>
 * <p>Interval is scheduled before the onClick completes, so it has to run fast.
 * If it runs slow, it does not generate skipped onClicks.
 */
public class OnRepeatListener implements View.OnTouchListener {
    private int initialInterval;
    private final int normalInterval;
    private final View.OnClickListener clickListener;
    private View downView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (downView == null) {
                return;
            }
            removeMessages(0);
            sendEmptyMessageDelayed(0, normalInterval);
            clickListener.onClick(downView);
        }
    };

    /**
     * @param initialInterval The interval before first click event
     * @param normalInterval  The interval before second and subsequent click
     *                        events
     * @param clickListener   The OnClickListener, that will be called
     *                        periodically
     */
    public OnRepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
        if (clickListener == null) throw new IllegalArgumentException("null runnable");
        if (initialInterval < 0 || normalInterval < 0)
            throw new IllegalArgumentException("negative interval");
        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.setPressed(true);
                downView = view;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, initialInterval);
                clickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                view.setPressed(false);
                handler.removeMessages(0);
                downView = null;
                return true;
        }
        return false;
    }
}