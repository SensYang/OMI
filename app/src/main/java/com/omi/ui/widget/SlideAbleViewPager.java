package com.omi.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.omi.R;


/**
 * Created by SensYang on 2016/6/15 0015.
 */
public class SlideAbleViewPager extends ViewPager {
    /**
     * 控制页面是否可以左右滑动
     */
    private boolean slidenabled;

    public SlideAbleViewPager(Context context) {
        super(context);
    }

    public SlideAbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OmiTheme);
        slidenabled = typedArray.getBoolean(R.styleable.OmiTheme_slideAble, false);
        typedArray.recycle();
    }

    /**
     * 设置是否可以滑动
     */
    public void setSlidenAbled(boolean slidenabled) {
        this.slidenabled = slidenabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!slidenabled) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!slidenabled) {
            return false;
        }
        return super.onTouchEvent(arg0);
    }
}