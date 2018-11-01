package com.omi.ui.base;

import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.net.NoHttpUtil;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment implements Observable {
    protected View contentView;
    private WeakReferenceHandler handler = null;
    private boolean isPrepared;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (!isPrepared && getContentView() != null) {
            onFirstUserVisible();
            isPrepared = true;
        }
    }

    /**
     * 初始化界面
     */
    @SuppressLint("InflateParams")
    protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView != null && contentView.getParent() != null) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        if (contentView == null) {
            contentView = inflaterView(inflater, container, savedInstanceState);
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
        }
        return contentView;
    }

    @Override
    public void onDestroy() {
        NoHttpUtil.getInstance().cancel(getSign());
        contentView = null;
        super.onDestroy();
    }

    public WeakReferenceHandler getHandler() {
        if (handler == null) handler = new WeakReferenceHandler(this);
        return this.handler;
    }

    public View getContentView() {
        return contentView;
    }

    protected void handlerPacketMsg(Message msg) {
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public Object getSign() {
        return hashCode();
    }
}
