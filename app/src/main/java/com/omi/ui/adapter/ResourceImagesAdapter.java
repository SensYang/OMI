package com.omi.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.omi.ui.adapter.base.BaseSimpleAdapter;

/**
 * Created by SensYang on 2016/6/4 0004.
 */
public class ResourceImagesAdapter extends BaseSimpleAdapter<Integer> {
    @Override
    public View getView(ViewGroup parent, View convertView, int position) {
        if (convertView == null) {
            convertView = new ImageView(parent.getContext());
        }
        ((ImageView) convertView).setImageResource(getItem(position));
        return convertView;
    }
}