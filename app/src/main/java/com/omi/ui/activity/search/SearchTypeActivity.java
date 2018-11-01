package com.omi.ui.activity.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.omi.R;
import com.omi.databinding.ActivitySearchTypeBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.QrcardDialog;
import com.omi.utils.ToastUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by SensYang on 2017/04/07 13:52
 */

public class SearchTypeActivity extends BaseActivity {
    private ActivitySearchTypeBinding binding;
    private QrcardDialog qrcardDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_type);
        binding.setSearchTypeActivity(this);
        qrcardDialog = new QrcardDialog(this);
        qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
    }

    public void onFinishClick(View view) {
        finish();
    }

    /**
     * 进入搜索
     */
    public void goSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    /**
     * 展示自己的二维码
     */
    public void showMyQr(View view) {
        if (qrcardDialog.getUser() == null) {
            qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
        }
        qrcardDialog.show();
    }

    /**
     * 进入二维码扫描
     */
    public void qrScan(View view) {
        startActivityForResult(new Intent(this, CaptureActivity.class), CaptureActivity.ACTION_QR);
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