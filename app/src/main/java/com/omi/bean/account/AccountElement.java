package com.omi.bean.account;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.InputType;

import com.omi.BR;

/**
 * Created by SensYang on 2017/04/02 21:19
 */

public class AccountElement extends BaseObservable {
    private String element;
    private String hint;
    private String action;
    private int inputType = InputType.TYPE_NULL;
    private boolean clickAble = true;

    @Bindable
    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Bindable
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (this.action != null && this.action.equalsIgnoreCase(action)) return;
        this.action = action;
        notifyPropertyChanged(BR.action);
    }

    @Bindable
    public boolean isClickAble() {
        return clickAble;
    }

    public void setClickAble(boolean clickAble) {
        if (this.clickAble == clickAble) return;
        this.clickAble = clickAble;
        notifyPropertyChanged(BR.clickAble);
    }

    @Bindable
    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        notifyPropertyChanged(BR.inputType);
    }

    public void onClick() {
        if (onActionClickListener != null) {
            onActionClickListener.onActionClick(this);
        }
    }

    private OnActionClickListener onActionClickListener;

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }

    public interface OnActionClickListener {
        void onActionClick(AccountElement element);
    }
}
