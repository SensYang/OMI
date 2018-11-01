package com.omi.ui.adapter.base;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import com.omi.R;
import com.omi.ui.widget.listener.OnItemClickListener;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by SensYang on 2016/6/14 0014.
 */
public abstract class BaseSimpleAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener, ListAdapter, SpinnerAdapter {
    private ArrayList<T> dataList = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener;
    private int count = 0;

    public void addData(int location, T data) {
        if (data != null)
            dataList.add(location, data);
    }

    public void addData(T data) {
        if (data != null)
            dataList.add(data);
    }

    @SafeVarargs
    public final void addData(T... data) {
        if (data != null) {
            for (T t : data) {
                if (t != null) {
                    dataList.add(t);
                }
            }
        }
    }

    public void addData(Collection<? extends T> list) {
        if (list != null)
            dataList.addAll(list);
    }

    public T removeData(int position) {
        return dataList.remove(position);
    }

    public void removeData(T data) {
        if (data != null)
            dataList.remove(data);
    }

    @SafeVarargs
    public final void removeData(T... data) {
        if (data != null) {
            for (T t : data) {
                if (t != null) {
                    dataList.remove(t);
                }
            }
        }
    }

    public void removeData(Collection<? extends T> list) {
        if (list != null)
            dataList.removeAll(list);
    }

    public T[] toDataArray(T[] array) {
        return dataList.toArray(array);
    }

    public void sortList() {
        if (dataList.size() > 1 && (dataList.get(0) instanceof Comparable)) {
            Collections.sort((List<Comparable>) dataList);
        }
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void clear() {
        dataList.clear();
    }

    @Override
    public int getCount() {
        count = dataList.size();
        if (count == 0) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
            }
            if (notEmptyView != null)
                notEmptyView.setVisibility(View.GONE);
        } else {
            if (emptyView != null)
                emptyView.setVisibility(View.GONE);
            if (notEmptyView != null) {
                notEmptyView.setVisibility(View.VISIBLE);
            }
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (convertViewMap.get(position) != null) {
            View view = convertViewMap.get(position).get();
            if (view != null) {
                container.removeView(view);
            }
        }
    }

    SparseArray<SoftReference<View>> convertViewMap = new SparseArray<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = null;
        int removeKey = -1;
        for (int i = 0; i < convertViewMap.size(); i++) {
            int key = convertViewMap.keyAt(i);
            View view = convertViewMap.get(key).get();
            if (view == null) {
                removeKey = key;
            } else if (view.getParent() == null) {
                removeKey = key;
                convertView = view;
            }
        }
        if (removeKey != -1) convertViewMap.remove(removeKey);
        convertView = getView(position, convertView, container);
        convertViewMap.put(position, new SoftReference<>(convertView));
        container.addView(convertView);
        return convertView;
    }

    /**
     * 请不要重写该方法 直接复写 {@link #getView(ViewGroup, View, int)}即可
     */
    @Deprecated
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getView(parent, convertView, position);
        if (onItemClickListener != null) {
            convertView.setOnClickListener(onClickListener);
            convertView.setTag(R.id.tagItemPosition, position);
        }
        return convertView;
    }

    public abstract View getView(ViewGroup parent, View convertView, int position);

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int clickPosition = (int) v.getTag(R.id.tagItemPosition);
                onItemClickListener.onItemClick(v, getItem(clickPosition), clickPosition);
            }
        }
    };

    @Override
    public T getItem(int position) {
        if (count != 0)
            return dataList.get(position % count);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View emptyView;

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    private View notEmptyView;

    public void setNotEmptyView(View notEmptyView) {
        this.notEmptyView = notEmptyView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
