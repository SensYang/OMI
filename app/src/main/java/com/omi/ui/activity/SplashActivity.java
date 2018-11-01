package com.omi.ui.activity;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;

import com.hyphenate.chat.EMClient;
import com.omi.BR;
import com.omi.R;
import com.omi.bean.User;
import com.omi.database.GlobalSharedPreferences;
import com.omi.database.LiteOrmDBUtil;
import com.omi.database.PreferencesSetting;
import com.omi.databinding.ActivitySplashBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.account.LoginActivity;
import com.omi.ui.activity.main.MainActivity;
import com.omi.ui.base.BaseActivity;

/**
 * Created by SensYang on 2017/04/08 23:47
 */

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding binding;
    private int second = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.setSplashActivity(this);
        getHandler().sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        if (msg.what <= 0) {
            goNext();
            return;
        }
        setSecond(msg.what);
        getHandler().sendEmptyMessageDelayed(--msg.what, 1000);
    }

    @Bindable
    public String getSecond() {
        return String.valueOf(second);
    }

    public void setSecond(int second) {
        if (second == this.second) return;
        this.second = second;
        notifyPropertyChanged(BR.second);
    }

    private void goNext() {
        startActivity(new Intent(this, MainActivity.class));
        if (!GlobalSharedPreferences.getInstance().getBoolean(PreferencesSetting.HAS_GUIDE.getName(), (Boolean) PreferencesSetting.HAS_GUIDE.getDefaultValue())) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }
        ApiByHttp.getInstance().setPhone(GlobalSharedPreferences.getInstance().getString(PreferencesSetting.LOGINED_PHONE.getName(), (String) PreferencesSetting.LOGINED_PHONE.getDefaultValue()));
        User.Data user = null;
        if (ApiByHttp.getInstance().getPhone() != null)
            user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", ApiByHttp.getInstance().getPhone());
        if (!EMClient.getInstance().isLoggedInBefore()) {
            ApiByHttp.getInstance().setPhone(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            if (user == null || user.getMember_avatar() == null || user.getMember_avatar().length() == 0) {
                ApiByHttp.getInstance().setPhone(null);
                EMClient.getInstance().logout(false, null);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }
        }
        ApiByHttp.getInstance().setMember_id(GlobalSharedPreferences.getInstance().getString(PreferencesSetting.LOGINED_MEMBER_ID.getName(), (String) PreferencesSetting.LOGINED_MEMBER_ID.getDefaultValue()));
        ApiByHttp.getInstance().setUser(user);
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        finish();
    }
}