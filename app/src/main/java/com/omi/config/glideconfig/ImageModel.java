package com.omi.config.glideconfig;

import android.annotation.SuppressLint;

/**
 * Created by SensYang on 2016/7/4 0004.
 */
public class ImageModel implements GlideDataModel {
    private String imgUrl;
    private int radius = -1;

    public ImageModel(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @param radius 默认为-1矩形 0为圆形 >0为圆角矩形
     */
    public ImageModel(String imgUrl, int radius) {
        this.imgUrl = imgUrl;
        this.radius = radius;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String buildDataModelUrl(int width, int height) {
        //拼装阿里云OSS图片下载的参数
        String url = imgUrl == null ? "" : imgUrl;
        switch (radius) {
            case -2://等比缩放矩形
                url += String.format("?x-oss-process=image/resize,w_%d,h_%d", width, height);
                break;
            case -1://等比放大矩形
                url += String.format("?x-oss-process=image/resize,m_fill,w_%d,h_%d", width, height);
                break;
            case 0://圆
                radius = Math.min(width, height);
                url += String.format("?x-oss-process=image/resize,m_fill,w_%d,h_%d/circle,r_%d/format,png", radius, radius, radius);
                break;
            default:
                if (radius > 0) {//圆角矩形
                    url += String.format("?x-oss-process=image/resize,m_fill,w_%d,h_%d/rounded-corners,r_%d/format,png", width, height, radius);
                } else {
                    String radiusString = radius + "";
                    int type = Integer.parseInt(radiusString.substring(0, 2));
                    int scaleSize = Integer.parseInt(radiusString.substring(2));
                    switch (type) {
                        case -2://等比缩放矩形
                            url += String.format("?x-oss-process=image/resize,w_%d,h_%d", width * scaleSize, height * scaleSize);
                            break;
                        case -1://等比放大矩形
                            url += String.format("?x-oss-process=image/resize,m_fill,w_%d,h_%d", width * scaleSize, height * scaleSize);
                            break;
                    }
                }
                break;
        }
        return url;
    }
}