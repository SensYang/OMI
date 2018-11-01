package com.omi.ui.widget.listener;

import android.view.View;

/**
 * Created by SensYang on 2016/7/9 0009.
 */
public interface OnItemClickListener<T> {
    /**
     * 点击的数据
     */
    void onItemClick(View view, T data, int position);
}
