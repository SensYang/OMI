package com.omi.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SensYang on 2015/1/30.
 */
public class SlideTextView extends TextView {

    public SlideTextView(Context context) {
        super(context);
    }

    public SlideTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textSize = getTextSize();
        getPaint().setStrokeWidth(textSize / 20);
        canvas.drawLine(0, getMeasuredHeight() / 2 + textSize / 10, getMeasuredWidth(), getMeasuredHeight() / 2 + textSize / 10, getPaint());
    }
}
