package com.omi.ui.widget.window.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.omi.R;
import com.omi.ui.base.BaseActivity;


/**
 * Created by SensYang on 2016/7/13 0013.
 */
public class BaseDialog extends Dialog implements Observable {
    private transient PropertyChangeRegistry mCallbacks;

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }


    private BaseActivity activity;
    private float alpha = 1;
    private Object tag;

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public void setShowingAlpha(float alpha) {
        this.alpha = alpha;
    }

    public BaseDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof BaseActivity) this.activity = (BaseActivity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
    }

    public Context getActivity() {
        if (activity != null) return activity;
        else return super.getContext();
    }

    @Override
    public void show() {
        handler.removeMessages(0);
        handler.sendEmptyMessage(1);
    }

    public void showDelayed(long time) {
        if (time == -1) handler.sendEmptyMessageDelayed(0, 10 * 1000);
        else handler.sendEmptyMessageDelayed(0, time);
        this.show();
    }

    @Override
    public void dismiss() {
        handler.removeMessages(0);
        handler.sendEmptyMessage(2);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!isShowing()) return;
                    dismiss();
                    break;
                case 1:
                    if (isShowing()) return;
                    BaseDialog.super.show();
                    if (activity != null) {
                        if (activity.isFinishing()) return;
                        if (alpha != 1) {
                            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                            params.alpha = alpha;
                            activity.getWindow().setAttributes(params);
                        }
                    }
                    break;
                case 2:
                    if (!isShowing()) return;
                    BaseDialog.super.dismiss();
                    if (activity == null || activity.isFinishing()) return;
                    if (alpha != 1) {
                        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                        params.alpha = 1f;
                        activity.getWindow().setAttributes(params);
                    }
                    break;

            }
        }
    };
}
