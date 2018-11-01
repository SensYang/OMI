package com.omi.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omi.config.databinding.ImageAttrAdapter;
import com.omi.ui.adapter.base.BaseSimpleAdapter;
import com.omi.ui.widget.ZoomImageView;
import com.omi.ui.widget.listener.OnItemClickListener;

/**
 * Created by SensYang on 2016/6/4 0004.
 */
public class ImagesAdapter extends BaseSimpleAdapter<String> implements OnItemClickListener<String> {
    /**
     * 是否支持循环播放
     */
    private boolean isLoop = false;
    /**
     * 图片圆角
     */
    private int imageRadius = -1;
    private boolean isCenterCrop = false;
    private TextView positionTV;

    /**
     * @param positionTV 导航
     * @param isLoop     是否要循环
     */
    public ImagesAdapter(TextView positionTV, boolean isLoop) {
        this.positionTV = positionTV;
        this.isLoop = isLoop;
        setOnItemClickListener(this);
    }

    public void setCenterCrop(boolean centerCrop) {
        isCenterCrop = centerCrop;
    }

    /**
     * @param imageRadius 设置图片的圆角
     *                    -2 等比缩放矩形
     *                    -1 等比放大矩形
     *                    0 圆形
     *                    >0 具体的圆角矩形
     *                    默认 -1
     */
    public void setImageRadius(int imageRadius) {
        this.imageRadius = imageRadius;
    }


    @Override
    public int getCount() {
        int count = super.getCount();
        if (isLoop) return count > 0 ? Integer.MAX_VALUE : 0;
        else return count;
    }

    public int getPosition(int position) {
        return position % super.getCount();
    }

    @Override
    public View getView(ViewGroup parent, View convertView, int position) {
        if (convertView == null) {
            convertView = new ZoomImageView(parent.getContext());
            convertView.setBackgroundColor(Color.BLACK);
            if (isCenterCrop) {
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
        if (imageRadius > -1) {
            ImageAttrAdapter.setSrc((ImageView) convertView, getItem(position), imageRadius, imageRadius, imageRadius, imageRadius);
        } else {
            ImageAttrAdapter.setSrc((ImageView) convertView, getItem(position));
        }
        return convertView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPageSelected(int position) {
        positionTV.setText((position + 1) + "/" + getCount());
    }

    @Override
    public void onItemClick(View view, String data, int position) {
        ((Activity) view.getContext()).finish();
    }
}
