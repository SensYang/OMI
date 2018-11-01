package com.omi.ui.activity.near;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.omi.R;
import com.omi.bean.User;
import com.omi.databinding.ActivityNearPeopleBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.adapter.near.NearPeopleAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.PeopleScreenDialog;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.utils.amap.LocationUtil;

/**
 * Created by SensYang on 2017/04/26 14:34
 */

public class NearPeopleActivity extends BaseActivity implements OnAlterClickListener {
    private ActivityNearPeopleBinding binding;
    private NearPeopleAdapter adapter;
    private int minage = 0;
    private int maxage = 200;
    private String sex;
    private PeopleScreenDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationUtil.getInstance().setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //TODO 更新位置
                ApiByHttp.getInstance().updateLocation(LocationUtil.getInstance().getLocation().getLongitude(), LocationUtil.getInstance().getLocation().getLatitude());
                LocationUtil.getInstance().stopLocation();
                LocationUtil.getInstance().setLocationListener(null);
            }
        });
        LocationUtil.getInstance().startLocation();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_near_people);
        binding.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NearPeopleAdapter();
        binding.setAdapter(adapter);
        dialog = new PeopleScreenDialog(this);
        dialog.setOnAlterClickListener(this);
        getData();
    }

    private void getData() {
        ApiByHttp.getInstance().getNearPeople(minage, maxage, sex, callback);
    }

    private ResponseCallback<User> callback = new ResponseCallback<User>() {
        @Override
        public void onSucceed(int what, User response) throws Exception {
            adapter.clear();
            adapter.addUser(response.getDatalist());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    @Override
    public void topRightClick(View view) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onConfirmClick() {
        minage = dialog.getMinage();
        maxage = dialog.getMaxage();
        sex = dialog.getSex();
        getData();
    }
}
