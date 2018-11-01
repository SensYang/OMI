package com.omi.ui.activity.more;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.omi.R;
import com.omi.databinding.ActivitySettingBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ToastUtil;

/**
 * Created by SensYang on 2017/04/12 13:32
 */

public class SettingActivity extends BaseActivity {
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setSettingActivity(this);
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    public void onLogoutClick(View view) {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                ApiByHttp.getInstance().setMember_id(null);
                ApiByHttp.getInstance().setPhone(null);
                ApiByHttp.getInstance().setUser(null);
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastUtil.showToast("退出登录失败");
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
