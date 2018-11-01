package com.omi.ui.widget.window.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.omi.R;

/**
 * Created by SensYang on 2016/7/9 0009.
 */
public abstract class BasePopWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private OnDismissListener onDismissListener;
    private Context context;
    private float showingAlpha = 1;

    public Context getContext() {
        return context;
    }

    public void setShowingAlpha(float showingAlpha) {
        this.showingAlpha = showingAlpha;
        setAlpha();
    }

    public BasePopWindow(Context context) {
        super(context);
        this.context = context;
        super.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setOutsideTouchable(true);    //触摸外部，PopupWindow不消失
        super.setBackgroundDrawable(new ColorDrawable(0x55000000));//无背景  就无法响应外部点击事件
        super.setFocusable(true);//获取焦点 否则EditText无法响应键盘弹出
        super.setAnimationStyle(R.style.nonePopAnim);
        super.setOnDismissListener(this);
        super.update();
    }

    @Override
    public void showAsDropDown(View anchor, int gravity, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setAlpha();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int xoff, int yoff) {
        super.showAtLocation(parent, gravity, xoff, yoff);
        setAlpha();
    }

    private void setAlpha() {
        if (isShowing() && context != null && context instanceof Activity) {
            if (showingAlpha != 1) {
                WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
                params.alpha = showingAlpha;
                ((Activity) context).getWindow().setAttributes(params);
            }
        }
    }

    @Override
    public void onDismiss() {
        if (onDismissListener != null) onDismissListener.onDismiss();
        if (showingAlpha == 1) return;
        if (context == null || !(context instanceof Activity)) return;
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity) context).getWindow().setAttributes(params);
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
