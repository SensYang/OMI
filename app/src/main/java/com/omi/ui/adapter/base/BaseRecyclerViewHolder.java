package com.omi.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by SensYang on 2017/03/09 14:59
 */

public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T data);
}