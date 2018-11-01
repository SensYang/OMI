package com.omi.ui.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.omi.R;
import com.omi.bean.ApplicationState;
import com.omi.database.GlobalSharedPreferences;
import com.omi.database.PreferencesSetting;
import com.omi.databinding.ActivityMainBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.account.LoginActivity;
import com.omi.ui.activity.search.SearchActivity;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.base.BaseFragmentAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.MainTableView;
import com.omi.ui.widget.listener.MainFragmentProxy;
import com.omi.utils.Log;
import com.omi.utils.ToastUtil;
import com.omi.utils.amap.LocationUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, MainTableView.OnTableChangedListener {
    private ActivityMainBinding mainBinding;
    private ApplicationState applicationState = new ApplicationState();
    private int currentAt = 0;
    private Intent homeIntent;
    private Fragment fragment[] = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        fragment[0] = new ConversationFragment();
        fragment[1] = new ContactsFragment(applicationState);
        fragment[2] = new PhoneFragment();
        fragment[3] = new DiscoverFragment();
        fragment[4] = new MyselfFragment();
        fragmentAdapter.addData(fragment);
        mainBinding.viewPager.setAdapter(fragmentAdapter);
        mainBinding.viewPager.setCurrentItem(currentAt);
        mainBinding.viewPager.addOnPageChangeListener(this);
        mainBinding.mainTableView.setOnTableChangedListener(this);
        EMClient.getInstance().addConnectionListener(new ConnectionListener());
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    @Override
    protected void onResume() {
        if (ApiByHttp.getInstance().getPhone() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            LocationUtil.getInstance().setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    Log.e("更新位置更新位置更新位置");
                    ApiByHttp.getInstance().updateLocation(LocationUtil.getInstance().getLocation().getLongitude(), LocationUtil.getInstance().getLocation().getLatitude());
                    LocationUtil.getInstance().stopLocation();
                    LocationUtil.getInstance().setLocationListener(null);
                }
            });
            LocationUtil.getInstance().startLocation();
        }
        super.onResume();
    }

    //实现ConnectionListener接口
    private class ConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.e("环信连接成功");
        }

        @Override
        public void onDisconnected(int error) {
            Log.e("环信连接断开" + error);
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
                ToastUtil.showLongToast("帐号不存在请重新申请");
                EMClient.getInstance().logout(true);
                GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PHONE.getName(), (String) PreferencesSetting.LOGINED_PHONE.getDefaultValue());
                GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PASS.getName(), (String) PreferencesSetting.LOGINED_PASS.getDefaultValue());
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登录
                ToastUtil.showLongToast("帐号在其他设备登录");
                EMClient.getInstance().logout(true);
                GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PHONE.getName(), (String) PreferencesSetting.LOGINED_PHONE.getDefaultValue());
                GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PASS.getName(), (String) PreferencesSetting.LOGINED_PASS.getDefaultValue());
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(homeIntent);
    }

    @Override
    public void onTableChanged(int index) {
        if (currentAt != index) {
            currentAt = index;
            mainBinding.viewPager.setCurrentItem(index, false);
        } else {
            if (fragment[index] instanceof MainFragmentProxy) {
                ((MainFragmentProxy) fragment[index]).scrollToTop();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mainBinding.mainTableView != null)
            mainBinding.mainTableView.onClick(mainBinding.viewPager.getCurrentItem());
        if (applicationState.isMainSliding() && position == 1) {
            applicationState.setMainSliding(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (!applicationState.isMainSliding() && state != ViewPager.SCROLL_STATE_IDLE) {
            applicationState.setMainSliding(true);
        }
    }

    /**
     * 进入搜索
     */
    public void goSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CaptureActivity.ACTION_QR && data != null) {
            String result = data.getStringExtra("result");
            if (result != null && result.startsWith("OMI_")) {
                String userid = result.substring("OMI_".length());
                if (userid.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
                    Intent intent = new Intent(this, SelfInfoActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, UserInfoActivity.class);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
            } else {
                ToastUtil.showToast(R.string.wrong_qr);
            }
        }
    }
}