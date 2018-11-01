package com.omi.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.omi.R;
import com.omi.databinding.WidgetMainTableViewBinding;

/**
 * Created by SensYang on 2017/03/08 10:26
 */

public class MainTableView extends LinearLayout {
    private WidgetMainTableViewBinding tableViewBinding;
    private OnTableChangedListener OnTableChangedListener;
    private int lastIndex = 0;
    private ImageView imgs[] = new ImageView[5];
    private int defaultImgRes[] = new int[5];
    private int selectImgRes[] = new int[5];

    public MainTableView(Context context) {
        this(context, null);
    }

    public MainTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tableViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_main_table_view, this, true);
        tableViewBinding.setPresenter(this);
        imgs[0] = tableViewBinding.tableMessageIV;
        imgs[1] = tableViewBinding.tableContactsIV;
        imgs[2] = tableViewBinding.tablePhoneIV;
        imgs[3] = tableViewBinding.tableDiscoverIV;
        imgs[4] = tableViewBinding.tableMyselfIV;
        defaultImgRes[0] = R.mipmap.main_table_message;
        selectImgRes[0] = R.mipmap.main_table_messageed;
        defaultImgRes[1] = R.mipmap.main_table_contacts;
        selectImgRes[1] = R.mipmap.main_table_contactsed;
        defaultImgRes[2] = R.mipmap.main_table_phone;
        selectImgRes[2] = R.mipmap.main_table_phoneed;
        defaultImgRes[3] = R.mipmap.main_table_discover;
        selectImgRes[3] = R.mipmap.main_table_discovered;
        defaultImgRes[4] = R.mipmap.main_table_myself;
        selectImgRes[4] = R.mipmap.main_table_myselfed;
    }

    public void onClick(View view) {
        int clickAt = Integer.parseInt(view.getTag() + "");
        onClick(clickAt);
    }

    /**
     * 手动触发点击事件
     */
    public void onClick(int index) {
        if (lastIndex == index) return;
        imgs[lastIndex].setImageResource(defaultImgRes[lastIndex]);
        imgs[index].setImageResource(selectImgRes[index]);
        lastIndex = index;
        if (OnTableChangedListener != null) OnTableChangedListener.onTableChanged(index);
    }

    public void setOnTableChangedListener(OnTableChangedListener onTableChangedListener) {
        this.OnTableChangedListener = onTableChangedListener;
    }

    public interface OnTableChangedListener {
        void onTableChanged(int index);
    }
}
