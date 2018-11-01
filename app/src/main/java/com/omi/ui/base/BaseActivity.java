package com.omi.ui.base;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.omi.OmiApplication;
import com.omi.net.NoHttpUtil;
import com.omi.utils.PermissionUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity的基类
 */
@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity implements Observable {
    private WeakReferenceHandler handler;
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
    protected void onCreate(Bundle savedInstanceState) {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //设置为竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        OmiApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("hasClosed", true);
        super.onSaveInstanceState(outState);
    }

    public WeakReferenceHandler getHandler() {
        if (handler == null) handler = new WeakReferenceHandler(this);
        return this.handler;
    }

    protected void handlerPacketMsg(Message msg) {
    }

    protected void showFragment(int contentId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(contentId, fragment).commitAllowingStateLoss();
    }

    public void topLeftClick(View view) {
    }

    public void topRightClick(View view) {
    }


    @Override
    protected void onDestroy() {
        NoHttpUtil.getInstance().cancel(getSign());
        OmiApplication.getInstance().removeActivity(this);
        ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);
        View v = vg.getChildAt(0);
        vg.removeView(v);
        handler = null;
        Glide.get(getApplicationContext()).clearMemory();
        super.onDestroy();
        System.gc();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isPermission = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                isPermission = false;
                break;
            }
        }
        PermissionUtil.PermissionCode permissionCode = null;
        for (String permission : permissions) {
            if (permissionCode != null) break;
            permissionCode = PermissionUtil.PermissionCode.parseOf(permission);
        }
        if (permissionCode != null)
            onPermissionCallback(permissionCode, isPermission);
    }

    /**
     * 权限申请回调
     * 子类可以重写该方法
     * 重要！！！：此方法只在安卓6.0以后有效 为了适应安卓6.0以后的权限管理机制
     *
     * @param permissionCode 对应的权限
     * @param isPermission   当前的权限是否被授权
     */
    protected void onPermissionCallback(PermissionUtil.PermissionCode permissionCode, boolean isPermission) {

    }

    public Object getSign() {
        return hashCode();
    }

}