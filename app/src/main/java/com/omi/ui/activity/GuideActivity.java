package com.omi.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.omi.R;
import com.omi.database.GlobalSharedPreferences;
import com.omi.database.PreferencesSetting;
import com.omi.databinding.ActivityGuideBinding;
import com.omi.ui.activity.account.LoginActivity;
import com.omi.ui.adapter.ResourceImagesAdapter;
import com.omi.ui.base.BaseActivity;

/**
 * Created by SensYang on 2017/04/03 17:56
 */

public class GuideActivity extends BaseActivity {
    private ActivityGuideBinding binding;
    private boolean loginAble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide);
        binding.setGuideActivity(this);
        binding.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 3) loginAble = true;
                else loginAble = false;
            }
        });
        ResourceImagesAdapter adapter = new ResourceImagesAdapter();
        adapter.addData(R.mipmap.guide_0);
        adapter.addData(R.mipmap.guide_1);
        adapter.addData(R.mipmap.guide_2);
        adapter.addData(R.mipmap.guide_3);
        binding.setAdapter(adapter);
    }

    public void goLogin(View view) {
        if (loginAble) {
            GlobalSharedPreferences.getInstance().putBoolean(PreferencesSetting.HAS_GUIDE.getName(), true);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
