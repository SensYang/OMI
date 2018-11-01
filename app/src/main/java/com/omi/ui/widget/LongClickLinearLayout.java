package com.omi.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by SensYang on 2017/04/21 18:36
 */
public class LongClickLinearLayout extends LinearLayout {
    public LongClickLinearLayout(Context context) {
        this(context, null);
    }

    public LongClickLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LongClickLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private int mTouchSlop;
    private boolean isLongClick = false;
    private static int LONG_CLICK_TIME = 600;
    private float downX = 0;
    private float downY = 0;
    private boolean interceptAfter = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (longClickListener == null) return super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                isLongClick = true;
                interceptAfter = false;
                handler.removeCallbacks(longClickRunnable);
                handler.postDelayed(longClickRunnable, LONG_CLICK_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                //当横移或纵移的长度大于系统规定的滑动最短距离时，则视为用户取消了longclick事件
                if (Math.abs(event.getX() - downX) > mTouchSlop || Math.abs(event.getY() - downY) > mTouchSlop) {
                    isLongClick = false;
                }
                if (interceptAfter) return true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (interceptAfter) return true;
                isLongClick = false;
                break;
        }
        boolean isDispatch = super.dispatchTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isDispatch) {
            //当down事件返回false时 不触发up事件 所以返回true强制触发UP事件，否则会出现click父布局出现longclick的效果
            return true;
        }
        return isDispatch;
    }

    private Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (isLongClick) {
                longClickListener.onLongClick(LongClickLinearLayout.this);
                interceptAfter = true;
            }
        }
    };

    private Handler handler = new Handler();

    private OnLongClickListener longClickListener;

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        longClickListener = l;
    }
}
