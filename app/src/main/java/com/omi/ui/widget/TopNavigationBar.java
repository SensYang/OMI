package com.omi.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.omi.R;
import com.omi.config.Config;
import com.omi.databinding.WidgetTopNavigationBarBinding;
import com.omi.ui.widget.listener.DeclaredOnClickListener;
import com.omi.utils.DisplayUtil;

/**
 * Created by SensYang on 2016/6/3.
 */
public class TopNavigationBar extends FrameLayout {
    private boolean hasSetStatusBarHeight = false;
    private float alpha = 1;
    private WidgetTopNavigationBarBinding navigationBarBinding;

    public TopNavigationBar(Context context) {
        this(context, null);
    }

    public TopNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        int color = Config.getMain_color();
        if (color != -1) setBackgroundColor(color);
        else setBackgroundResource(R.color.main_color);
        navigationBarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_top_navigation_bar, this, true);
        // 初始化属性
        this.initializeAttributes(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!hasSetStatusBarHeight) {
            hasSetStatusBarHeight = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int left = getPaddingLeft();
                int top = getPaddingTop() + DisplayUtil.getStatusBarHeight();
                int right = getPaddingRight();
                int bottom = getPaddingBottom();
                setPadding(left, top, right, bottom);
                getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            }
            setBackgroundAlpha(alpha);
        }
    }

    public void setBackgroundAlpha(float alpha) {
        this.alpha = alpha;
        if (getBackground() != null) {
            getBackground().setAlpha((int) (alpha * 255));
        }
    }

    public void setLeftClick(String methodName) {
        if (methodName != null) {
            navigationBarBinding.leftRL.setOnClickListener(new DeclaredOnClickListener(navigationBarBinding.leftRL, methodName));
        }
    }

    public void setRightClick(String methodName) {
        if (methodName != null) {
            navigationBarBinding.rightLL.setOnClickListener(new DeclaredOnClickListener(navigationBarBinding.rightLL, methodName));
        }
    }

    public void setLeftClick(OnClickListener listener) {
        navigationBarBinding.leftRL.setOnClickListener(listener);
    }

    public void setRightClick(OnClickListener listener) {
        navigationBarBinding.rightLL.setOnClickListener(listener);
    }

    public int calculateHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            return getResources().getDimensionPixelOffset(R.dimen.topbar_height) + DisplayUtil.getStatusBarHeight();
        else return getResources().getDimensionPixelOffset(R.dimen.topbar_height);
    }

    private void initializeAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OmiTheme);
        boolean visibility;
        int resourceId;
        resourceId = typedArray.getResourceId(R.styleable.OmiTheme_backGround, -1);
        if (resourceId != -1) {
            this.setBackgroundResource(resourceId);
        }
        navigationBarBinding.leftTV.setText(typedArray.getText(R.styleable.OmiTheme_leftText));
        visibility = typedArray.getBoolean(R.styleable.OmiTheme_leftVisible, true);
        if (!visibility) {
            navigationBarBinding.leftIV.setVisibility(View.GONE);
        }
        resourceId = typedArray.getResourceId(R.styleable.OmiTheme_leftSrc, -1);
        if (resourceId != -1) {
            navigationBarBinding.leftIV.setImageResource(resourceId);
        }

        navigationBarBinding.centerTV.setText(typedArray.getText(R.styleable.OmiTheme_centerText));

        visibility = typedArray.getBoolean(R.styleable.OmiTheme_rightVisible, true);
        resourceId = typedArray.getResourceId(R.styleable.OmiTheme_rightSrc, -1);
        navigationBarBinding.rightTV.setText(typedArray.getText(R.styleable.OmiTheme_rightText));
        if (!visibility) {
            navigationBarBinding.rightIV.setVisibility(View.GONE);
            navigationBarBinding.topBarRightSecond.setVisibility(View.GONE);
        }
        if (resourceId != -1) {
            navigationBarBinding.rightIV.setImageResource(resourceId);
        }
        resourceId = typedArray.getResourceId(R.styleable.OmiTheme_rightSecondSrc, -1);
        if (resourceId != -1) {
            navigationBarBinding.topBarRightSecond.setVisibility(View.VISIBLE);
            navigationBarBinding.topBarRightSecond.setImageResource(resourceId);
        }
        typedArray.recycle();
    }

    public TextView getLeftTV() {
        return navigationBarBinding.leftTV;
    }

    public ImageView getLeftIV() {
        return navigationBarBinding.leftIV;
    }

    public TextView getCenterTV() {
        return navigationBarBinding.centerTV;
    }

    public TextView getRightTV() {
        return navigationBarBinding.rightTV;
    }

    public ImageView getRightIV() {
        return navigationBarBinding.rightIV;
    }

    /**
     * 设置左边新消息显示状态
     */
    public void setLeftDotVisibility(boolean isVisibility) {
        navigationBarBinding.leftNewDot.setVisibility(isVisibility ? VISIBLE : GONE);
    }

    public void setLeftText(int textRes) {
        if (navigationBarBinding.leftTV != null) navigationBarBinding.leftTV.setText(textRes);
    }

    public void setLeftText(CharSequence text) {
        if (navigationBarBinding.leftTV != null) navigationBarBinding.leftTV.setText(text);
    }

    public void setCenterText(int textRes) {
        if (navigationBarBinding.centerTV != null) navigationBarBinding.centerTV.setText(textRes);
    }

    public void setCenterText(CharSequence text) {
        if (navigationBarBinding.centerTV != null) navigationBarBinding.centerTV.setText(text);
    }

    public void setRightText(int textRes) {
        if (navigationBarBinding.rightTV != null) navigationBarBinding.rightTV.setText(textRes);
    }

    public void setRightText(CharSequence text) {
        if (navigationBarBinding.rightTV != null) navigationBarBinding.rightTV.setText(text);
    }

    public void setRightImageResource(int resId) {
        if (navigationBarBinding.rightIV != null) {
            navigationBarBinding.rightIV.setImageResource(resId);
        }
    }
}
