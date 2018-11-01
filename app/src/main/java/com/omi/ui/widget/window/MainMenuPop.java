package com.omi.ui.widget.window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.databinding.PopMainMenuBinding;
import com.omi.ui.activity.contact.CreatGroupActivity;
import com.omi.ui.activity.search.SearchTypeActivity;
import com.omi.ui.widget.window.base.BasePopWindow;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by SensYang on 2017/04/07 11:18
 */
public class MainMenuPop extends BasePopWindow {
    PopMainMenuBinding binding;
    private Context context;

    public MainMenuPop(Context context) {
        super(context);
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_main_menu, null, false);
        binding.setMainMenuPop(this);
        setContentView(binding.getRoot());
        super.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        super.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor, Gravity.TOP | Gravity.START, 0, 0);
    }

    /**
     * 发起群聊
     */
    public void onGroupClick(View view) {
        getContext().startActivity(new Intent(getContext(), CreatGroupActivity.class));
        dismiss();
    }

    /**
     * 添加朋友
     */
    public void onAddClick(View view) {
        getContext().startActivity(new Intent(getContext(), SearchTypeActivity.class));
        dismiss();
    }

    /**
     * 扫一扫
     */
    public void onScanClick(View view) {
        dismiss();
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(new Intent(context, CaptureActivity.class), CaptureActivity.ACTION_QR);
        }
    }
}
