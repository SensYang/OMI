package com.omi.bean.account;

import android.databinding.Bindable;

import com.omi.BR;
import com.omi.bean.base.BaseBean;

/**
 * Created by SensYang on 2017/04/10 12:15
 */

public class ContactInvited extends BaseBean {
    private String username;
    private String nickName;
    private String userHead;
    private String reason;
    private boolean hasAdd;
    private boolean hasDecline;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        if (nickName == null && nickName.length() == 0) {
            this.nickName = null;
            return;
        }
        this.nickName = nickName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason == null && reason.length() == 0) {
            this.reason = null;
            return;
        }
        this.reason = reason;
    }

    @Bindable
    public boolean isHasAdd() {
        return hasAdd;
    }

    public void setHasAdd(boolean hasAdd) {
        if (hasAdd == this.hasAdd) return;
        this.hasAdd = hasAdd;
        notifyPropertyChanged(BR.hasAdd);
    }

    @Bindable
    public boolean isHasDecline() {
        return hasDecline;
    }

    public void setHasDecline(boolean hasDecline) {
        if (hasDecline == this.hasDecline) return;
        this.hasDecline = hasDecline;
        notifyPropertyChanged(BR.hasDecline);
    }
}
