package com.omi.ui.widget.window;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.omi.R;
import com.omi.databinding.DialogWaringBinding;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.base.BaseDialog;
import com.omi.utils.DisplayUtil;

/**
 * Created by SensYang on 2017/04/11 21:07
 */

public class WaringDialog extends BaseDialog {
    private OnAlterClickListener onAlterClickListener;
    private DialogWaringBinding binding;
    private Object tag;

    public WaringDialog(Context context) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_waring, null, false);
        binding.setWaringDialog(this);
        setContentView(binding.getRoot());
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) DisplayUtil.dip2px(200);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setTitleText(String text) {
        binding.setTitle(text);
    }

    public void setTitleText(int textRes) {
        binding.setTitle(getContext().getResources().getString(textRes));
    }

    public void setLeftText(String text) {
        binding.setLeftText(text);
    }

    public void setLeftText(int textRes) {
        binding.setLeftText(getContext().getResources().getString(textRes));
    }

    public void setRightText(String text) {
        binding.setRightText(text);
    }

    public void setRightText(int textRes) {
        binding.setRightText(getContext().getResources().getString(textRes));
    }

    public void onLeftClick(View view) {
        dismiss();
        if (onAlterClickListener != null) onAlterClickListener.onConfirmClick();
    }

    public void onRightClick(View view) {
        dismiss();
        if (onAlterClickListener != null) onAlterClickListener.onCancelClick();
    }

    public void setOnAlterClickListener(OnAlterClickListener onAlterClickListener) {
        this.onAlterClickListener = onAlterClickListener;
    }
}
