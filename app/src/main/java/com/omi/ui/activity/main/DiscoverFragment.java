package com.omi.ui.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.databinding.FragmentDiscoverBinding;
import com.omi.ui.activity.dynamic.DynamicActivity;
import com.omi.ui.activity.near.NearPeopleActivity;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.listener.MainFragmentProxy;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by SensYang on 2017/03/08 14:21
 */
public class DiscoverFragment extends BaseFragment implements MainFragmentProxy {
    FragmentDiscoverBinding discoverBinding;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        discoverBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);
        discoverBinding.setDiscoverClickCallback(this);
//        discoverBinding.topNavigationBar.setLeftDotVisibility(true);
        return discoverBinding.getRoot();
    }

    @Override
    public void onFirstUserVisible() {

    }

    public void onDynamicClick(View view) {
        startActivity(new Intent(getContext(), DynamicActivity.class));
    }

    public void onNearClick(View view) {
        startActivity(new Intent(getActivity(), NearPeopleActivity.class));
    }

    public void onQrScanClick(View view) {
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), CaptureActivity.ACTION_QR);
    }

    @Override
    public void scrollToTop() {
        discoverBinding.getRoot().scrollTo(0, 0);
    }
}
