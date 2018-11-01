package com.omi.ui.adapter.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/03/22 13:04
 */

public class BasePagerAdapter extends PagerAdapter implements AdapterProxy<View> {
    private List<View> dataList = new ArrayList<>();

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(dataList.get(position), 0);
        return dataList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(dataList.get(position));
    }

    @Override
    public void addData(View data) {
        if (data == null) return;
        dataList.add(data);
    }

    @Override
    public void addData(View... data) {
        if (data != null) {
            for (View view : data) {
                if (view != null) {
                    dataList.add(view);
                }
            }
        }
    }

    @Override
    public void addData(Collection<View> list) {
        if (list != null)
            dataList.addAll(list);
    }

    @Override
    public void clear() {
        dataList.clear();
    }
}
