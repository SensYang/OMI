package com.omi.ui.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakReferenceHandler extends Handler {
    private WeakReference<BaseActivity> mActivity = null;
    private WeakReference<BaseFragment> mFragment = null;

    public WeakReferenceHandler() {
        super(Looper.getMainLooper());
    }

    public WeakReferenceHandler(BaseActivity activity) {
        super(Looper.getMainLooper());
        mActivity = new WeakReference<>(activity);
    }

    public WeakReferenceHandler(BaseFragment fragment) {
        super(Looper.getMainLooper());
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (null != mActivity && null != mActivity.get()) {
            if (mActivity.get().isFinishing()) return;
            mActivity.get().handlerPacketMsg(msg);
        } else if (null != mFragment && null != mFragment.get()) {
            mFragment.get().handlerPacketMsg(msg);
        }
    }
}
