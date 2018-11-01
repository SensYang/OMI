package com.omi.ui.widget.indexlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.omi.R;

/**
 * Created by Administrator on 2015/5/6.
 */
public class QuickIndexBar extends View {

    private Paint paint;
    private Paint selectPaint;

    private TextView floatingView;

    private float centerx;

    private float height;

    private RecyclerView recyclerView;

    private char[] indexArr = new char[]{
            '↑', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};
    private char selectChar = 0;

    public void setFloatingView(TextView floatingView) {
        this.floatingView = floatingView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int textSize;
        int textColor;
        int selectTextColor;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OmiTheme);
        textSize = typedArray.getDimensionPixelSize(R.styleable.OmiTheme_textSize, 30);
        textColor = typedArray.getColor(R.styleable.OmiTheme_textColor, Color.DKGRAY);
        selectTextColor = typedArray.getColor(R.styleable.OmiTheme_selectTextColor, Color.DKGRAY);
        typedArray.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        selectPaint = new Paint(paint);
        selectPaint.setColor(selectTextColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (centerx == 0) {
            centerx = getWidth() / 2;
            height = getHeight() / indexArr.length;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            if (indexArr[i] == selectChar) {
                canvas.drawText(indexArr[i] + "", centerx, height * i + paint.getTextSize(), selectPaint);
            } else {
                canvas.drawText(indexArr[i] + "", centerx, height * i + paint.getTextSize(), paint);
            }
        }
    }

    private int sectionForPoint(float y) {
        int index = (int) (y / height);
        if (index < 0) {
            index = 0;
        }
        if (index > indexArr.length - 1) {
            index = indexArr.length - 1;
        }
        return index;
    }

    private void setHeaderTextAndScroll(MotionEvent event) {
        if (recyclerView == null) {
            return;
        }
        selectChar = indexArr[sectionForPoint(event.getY())];
        invalidate();
        floatingView.setText(selectChar + "");
        IndexerAdapter adapter;
        RecyclerView.Adapter listAdapter = recyclerView.getAdapter();
        if (listAdapter != null && listAdapter instanceof IndexerAdapter) {
            adapter = (IndexerAdapter) listAdapter;
            int position = adapter.getPositionForSection(selectChar);
            if (position != -1) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager)
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                else layoutManager.scrollToPosition(position);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (floatingView == null) {
                    floatingView = (TextView) ((View) getParent()).findViewById(R.id.floatingView);
                }
                setHeaderTextAndScroll(event);
                floatingView.setVisibility(View.VISIBLE);
                setBackgroundResource(R.drawable.sidebar_background_pressed);
                return true;
            case MotionEvent.ACTION_MOVE:
                setHeaderTextAndScroll(event);
                return true;
            case MotionEvent.ACTION_UP:
                selectChar = 0;
                floatingView.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                selectChar = 0;
                floatingView.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
