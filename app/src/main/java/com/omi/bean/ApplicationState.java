package com.omi.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.omi.BR;

/**
 * Created by SensYang on 2017/03/14 15:45
 */

public class ApplicationState extends BaseObservable {
    //主界面是否正在滑动
    private boolean isMainSliding;

    @Bindable
    public boolean isMainSliding() {
        return isMainSliding;
    }

    public void setMainSliding(boolean mainSliding) {
        isMainSliding = mainSliding;
        notifyPropertyChanged(BR.mainSliding);
    }
}
