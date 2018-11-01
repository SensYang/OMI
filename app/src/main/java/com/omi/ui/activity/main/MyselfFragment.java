package com.omi.ui.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.databinding.FragmentMyselfBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.dynamic.DynamicActivity;
import com.omi.ui.activity.more.SettingActivity;
import com.omi.ui.activity.otherApp.MoreAppActivity;
import com.omi.ui.activity.recharge.RechargeFlowActivity;
import com.omi.ui.activity.recharge.RechargePhoneActivity;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.utils.ImageBrowseActivity;
import com.omi.ui.activity.utils.WebDetailActivity;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.QrcardDialog;
import com.omi.ui.widget.listener.MainFragmentProxy;
import com.omi.utils.ToastUtil;

/**
 * Created by SensYang on 2017/03/08 14:02
 */

public class MyselfFragment extends BaseFragment implements MainFragmentProxy {
    private FragmentMyselfBinding myselfBinding;
    private QrcardDialog qrcardDialog;
    private Intent webIntent;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myselfBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_myself, container, false);
        myselfBinding.setMyselfFragment(this);
        myselfBinding.topNavigationBar.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        webIntent = new Intent(getActivity(), WebDetailActivity.class);
        return myselfBinding.getRoot();
    }

    @Override
    public void onUserVisible() {
        if (qrcardDialog != null) qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
    }

    @Override
    public void onFirstUserVisible() {
        if (qrcardDialog == null) {
            qrcardDialog = new QrcardDialog(getActivity());
            qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
        }
    }

    public void onHeadClick(View view) {
        User.Data user = ApiByHttp.getInstance().getUser();
        if (user != null && user.getMember_avatar() != null) {
            Intent intent = new Intent(getContext(), ImageBrowseActivity.class);
            intent.putExtra("imageurls", new String[]{user.getMember_avatar()});
            getContext().startActivity(intent);
        }
    }

    public void onSelfInfoClick(View view) {
        startActivity(new Intent(getActivity(), SelfInfoActivity.class));
    }

    /**
     * 点击二维码
     */
    public void onQrClick(View view) {
        qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
        qrcardDialog.show();
    }

    /**
     * 点击我的动态
     */
    public void onDynamicClick(View view) {
        Intent intent = new Intent(getActivity(), DynamicActivity.class);
        intent.putExtra("isSelf", true);
        startActivity(intent);
    }

    /**
     * 点击我的收藏
     */
    public void onCollectClick(View view) {
        ToastUtil.showToast("我的收藏");
    }

    /**
     * 点击流量充值
     */
    public void onFlowRechargeClick(View view) {
        view.getContext().startActivity(new Intent(getActivity(), RechargeFlowActivity.class));
    }

    /**
     * 点击电话卡充值
     */
    public void onPhoneRechargeClick(View view) {
        view.getContext().startActivity(new Intent(getActivity(), RechargePhoneActivity.class));
    }

    /**
     * 点击我的银行卡
     */
    public void onBankCardClick(View view) {
        ToastUtil.showToast("我的银行卡");
    }

    /**
     * 点击淘宝
     */
    public void onTaoBaoClick(View view) {
        webIntent.putExtra("url", "http://m.taobao.com/");
        startActivity(webIntent);
    }

    /**
     * 点击唯品会
     */
    public void onWeiPinHuiClick(View view) {
        webIntent.putExtra("url", "http://m.vip.com/");
        startActivity(webIntent);
    }

    /**
     * 点击京东
     */
    public void onJingDongClick(View view) {
        webIntent.putExtra("url", "http://m.jd.com/");
        startActivity(webIntent);
    }

    /**
     * 点击聚美
     */
    public void onJuMeiClick(View view) {
        webIntent.putExtra("url", "http://h5.jumei.com/");
        startActivity(webIntent);
    }

    /**
     * 点击更多
     */
    public void onMoreClick(View view) {
        startActivity(new Intent(getActivity(), MoreAppActivity.class));
    }

    @Override
    public void scrollToTop() {
        myselfBinding.scrollView.scrollTo(0, 0);
    }
}
