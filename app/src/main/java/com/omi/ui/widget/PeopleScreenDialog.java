package com.omi.ui.widget;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.omi.BR;
import com.omi.R;
import com.omi.databinding.DialogPeopleScreenBinding;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.base.BaseDialog;
import com.omi.utils.DisplayUtil;

/**
 * Created by SensYang on 2017/04/26 15:22
 */

public class PeopleScreenDialog extends BaseDialog {
    private DialogPeopleScreenBinding binding;
    private int minage = 0;
    private int maxage = 200;
    private String sex;
    private OnAlterClickListener onAlterClickListener;
    private int ageAt = 6;
    private boolean nearVisibility = false;

    public PeopleScreenDialog(Context context) {
        super(context);
        setShowingAlpha(0.5f);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_people_screen, null, false);
        binding.setScreenDialog(this);
        setContentView(binding.getRoot());
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DisplayUtil.getWidth() - DisplayUtil.dip2px(26));
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
    }

    public void setOnAlterClickListener(OnAlterClickListener onAlterClickListener) {
        this.onAlterClickListener = onAlterClickListener;
    }

    @Bindable
    public int getMinage() {
        return minage;
    }

    @Bindable
    public int getMaxage() {
        return maxage;
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex.length() > 1) sex = null;
        if (sex != null && sex.equalsIgnoreCase(this.sex)) return;
        this.sex = sex;
        notifyPropertyChanged(BR.sex);
    }

    @Bindable
    public int getAgeAt() {
        return ageAt;
    }

    public void setAgeAt(int ageAt) {
        if (ageAt == this.ageAt) return;
        this.ageAt = ageAt;
        notifyPropertyChanged(BR.ageAt);
        if (ageAt == 6) {
            minage = 0;
            maxage = 200;
        } else {
            minage = ageAt * 10;
            maxage = minage + 10;
        }
    }

    @Bindable
    public boolean isNearVisibility() {
        return nearVisibility;
    }

    public void setNearVisibility(boolean nearVisibility) {
        if (this.nearVisibility == nearVisibility) return;
        this.nearVisibility = nearVisibility;
        notifyPropertyChanged(BR.nearVisibility);
    }

    public void onCancelClick() {
        dismiss();
        if (onAlterClickListener != null) onAlterClickListener.onCancelClick();
    }

    public void onConfirmClick() {
        dismiss();
        if (onAlterClickListener != null) onAlterClickListener.onConfirmClick();
    }
}
