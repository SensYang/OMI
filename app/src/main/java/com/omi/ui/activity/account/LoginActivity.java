package com.omi.ui.activity.account;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.hyphenate.chat.EMClient;
import com.omi.BR;
import com.omi.R;
import com.omi.databinding.ActivityLoginBinding;
import com.omi.ui.adapter.base.BaseFragmentAdapter;
import com.omi.ui.base.BaseActivity;

/**
 * Created by SensYang on 2017/04/02 13:20
 */

public class LoginActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ActivityLoginBinding binding;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager());
        adapter.addData(new LoginFragment());
        adapter.addData(new RegisterFragment());
        binding.setAdapter(adapter);
        binding.setLoginActivity(this);
        EMClient.getInstance().logout(true);
    }

    @Bindable
    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
        notifyPropertyChanged(BR.currentItem);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == currentItem) return;
        setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

