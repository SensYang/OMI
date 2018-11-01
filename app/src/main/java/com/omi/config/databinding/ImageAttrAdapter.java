package com.omi.config.databinding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.omi.config.glideconfig.RoundTransform;
import com.omi.utils.Log;

/**
 * Created by SensYang on 2017/03/09 13:01
 */
public class ImageAttrAdapter {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView, String url) {
        setSrc(imageView,url,null);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView, Uri uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .signature(new StringSignature("" + System.currentTimeMillis()))
                .crossFade()
                .into(imageView);
    }

    @BindingAdapter({"android:src","omi:placeholder"})
    public static void setSrc(ImageView imageView, String url,int placeholder){
        setSrc(imageView,url,placeholder,null);
    }

    @BindingAdapter({"android:src","omi:placeholder"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder){
        setSrc(imageView,url,placeholder,null);
    }

    @BindingAdapter({"android:src","omi:error"})
    public static void _setSrc(ImageView imageView, String url,int error){
        setSrc(imageView,url,null,error);
    }

    @BindingAdapter({"android:src","omi:error"})
    public static void _setSrc(ImageView imageView, String url,Drawable error){
        setSrc(imageView,url,null,error);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder,Drawable error){
        Log.e("setSrc--->",url+"");
        if (url != null && url.endsWith(".gif")) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        }
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"})
    public static void setSrc(ImageView imageView, String url,int placeholder,Drawable error){
        Log.e("setSrc--->",url+"");
        if (url != null && url.endsWith(".gif")) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        }
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder, int error){
        Log.e("setSrc--->",url+"");
        if (url != null && url.endsWith(".gif")) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        }
    }


    @BindingAdapter({"android:src","omi:placeholder","omi:error"})
    public static void setSrc(ImageView imageView, String url,int placeholder,int error){
        Log.e("setSrc--->",url+"");
        if (url != null && url.endsWith(".gif")) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(placeholder)
                    .crossFade()
                    .error(error)
                    .into(imageView);
        }
    }

    @BindingAdapter({"android:src"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        setSrc(imageView, url, null, null,outsideWidth, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,int placeholder
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, null,outsideWidth, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, null,outsideWidth, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:error"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void _setSrc(ImageView imageView, String url,int error
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, null, error,outsideWidth, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:error"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void _setSrc(ImageView imageView, String url,Drawable error
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, null, error,outsideWidth, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder,Drawable error
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        Log.e("setSrc--->",url+"");
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new RoundTransform(imageView.getContext())
                        .setOutsideWidth(outsideWidth)
                        .setLeftTopRadius(leftTopRadius)
                        .setRightTopRadius(rightTopRadius)
                        .setRightBottomRadius(rightBottomRadius)
                        .setLeftBottomRadius(leftBottomRadius)
                )
                .placeholder(placeholder)
                .crossFade()
                .error(error)
                .into(imageView);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,int placeholder,Drawable error
            ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        Log.e("setSrc--->",url+"");
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new RoundTransform(imageView.getContext())
                        .setOutsideWidth(outsideWidth)
                        .setLeftTopRadius(leftTopRadius)
                        .setRightTopRadius(rightTopRadius)
                        .setRightBottomRadius(rightBottomRadius)
                        .setLeftBottomRadius(leftBottomRadius)
                )
                .placeholder(placeholder)
                .crossFade()
                .error(error)
                .into(imageView);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:outsideWidth"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder, int error
                                ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        Log.e("setSrc--->",url+"");
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new RoundTransform(imageView.getContext())
                        .setOutsideWidth(outsideWidth)
                        .setLeftTopRadius(leftTopRadius)
                        .setRightTopRadius(rightTopRadius)
                        .setRightBottomRadius(rightBottomRadius)
                        .setLeftBottomRadius(leftBottomRadius)
                )
                .placeholder(placeholder)
                .crossFade()
                .error(error)
                .into(imageView);
    }

    @BindingAdapter({"android:src"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        setSrc(imageView, url, null, null,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,int placeholder
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, null,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, null,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:error"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void _setSrc(ImageView imageView, String url,int error
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, null, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:error"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void _setSrc(ImageView imageView, String url,Drawable error
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, null, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder,Drawable error
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,int placeholder,Drawable error
            , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
            ,"omi:leftTopRadius"
            ,"omi:rightTopRadius"
            ,"omi:rightBottomRadius"
            ,"omi:leftBottomRadius"})
    public static void setSrc(ImageView imageView, String url,Drawable placeholder, int error
                                , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {
        setSrc(imageView, url, placeholder, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }


    @BindingAdapter({"android:src","omi:placeholder","omi:error"
                        ,"omi:leftTopRadius"
                        ,"omi:rightTopRadius"
                        ,"omi:rightBottomRadius"
                        ,"omi:leftBottomRadius"})
    public void setSrc(ImageView imageView, String url,int placeholder,int error
                       , float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        setSrc(imageView, url, placeholder, error,0, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
    }

    @BindingAdapter({"android:src","omi:placeholder","omi:error"
                        ,"omi:outsideWidth"
                        ,"omi:leftTopRadius"
                        ,"omi:rightTopRadius"
                        ,"omi:rightBottomRadius"
                        ,"omi:leftBottomRadius"})
    public void setSrc(ImageView imageView, String url,int placeholder,int error
                       ,float outsideWidth, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius){
        Log.e("setSrc--->",url+"");
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new RoundTransform(imageView.getContext())
                        .setOutsideWidth(outsideWidth)
                        .setLeftTopRadius(leftTopRadius)
                        .setRightTopRadius(rightTopRadius)
                        .setRightBottomRadius(rightBottomRadius)
                        .setLeftBottomRadius(leftBottomRadius)
                )
                .placeholder(placeholder)
                .crossFade()
                .error(error)
                .into(imageView);
    }


}
