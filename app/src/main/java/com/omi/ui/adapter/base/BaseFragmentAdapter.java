package com.omi.ui.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2016/7/7 0007.
 */
public class BaseFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, AdapterProxy<T> {
    private List<T> dataList = new ArrayList<>();

    public void addData(T data) {
        if (data != null)
            dataList.add(data);
    }

    public final void addData(T... data) {
        if (data != null) {
            for (T t : data) {
                if (t != null) {
                    dataList.add(t);
                }
            }
        }
    }

    public void addData(Collection<T> list) {
        if (list != null)
            dataList.addAll(list);
    }

    public void clear() {
        dataList.clear();
    }

    public BaseFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
