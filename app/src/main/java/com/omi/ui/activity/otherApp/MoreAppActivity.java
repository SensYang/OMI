package com.omi.ui.activity.otherApp;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.omi.BR;
import com.omi.R;
import com.omi.databinding.ActivityMoreAppBinding;
import com.omi.ui.activity.utils.WebDetailActivity;
import com.omi.ui.base.BaseActivity;

/**
 * Created by SensYang on 2017/04/26 12:54
 */

public class MoreAppActivity extends BaseActivity {
    ActivityMoreAppBinding binding;
    private Intent webIntent;
    private int pointAt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_app);
        binding.setMoreAppActivity(this);
        webIntent = new Intent(this, WebDetailActivity.class);
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    @Bindable
    public int getPointAt() {
        return pointAt;
    }

    public void setPointAt(int pointAt) {
        if (pointAt == this.pointAt) return;
        this.pointAt = pointAt;
        notifyPropertyChanged(BR.pointAt);
    }

    public void onTypeClick(View view) {
        View topV = (View) view.getTag();
        if (topV == null) return;
        binding.scrollView.scrollTo(0, topV.getTop());
        switch (topV.getId()){
            case R.id.type0:setPointAt(0);break;
            case R.id.type1:setPointAt(1);break;
            case R.id.type2:setPointAt(2);break;
            case R.id.type3:setPointAt(3);break;
            case R.id.type4:setPointAt(4);break;
        }
    }

    public void onAppClick(View view) {
        String url = (String) view.getTag();
        if (url == null) return;
        webIntent.putExtra("url", url);
        startActivity(webIntent);
    }
}
