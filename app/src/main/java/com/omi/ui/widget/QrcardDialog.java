package com.omi.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.omi.R;
import com.omi.bean.User;
import com.omi.config.glideconfig.RoundTransform;
import com.omi.databinding.DialogQrcardBinding;
import com.omi.ui.widget.window.base.BaseDialog;
import com.omi.utils.DisplayUtil;
import com.xys.libzxing.zxing.encoding.EncodingUtils;


/**
 * Created by SensYang on 2017/04/18 14:06
 */

public class QrcardDialog extends BaseDialog {
    private DialogQrcardBinding binding;
    private User.Data user;

    public QrcardDialog(Context context) {
        super(context);
        setShowingAlpha(0.8f);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_qrcard, null, false);
        setContentView(binding.getRoot());
    }

    public void setUser(User.Data data) {
        if (data == null) return;
        if (this.user != null && this.user.getMember_avatar() != null && this.user.getMember_avatar().equalsIgnoreCase(data.getMember_avatar()) && this.user.getMember_birthday() != null && this.user.getMember_birthday().equalsIgnoreCase(data.getMember_birthday()) && this.user.getMember_name() != null && this.user.getMember_name().equalsIgnoreCase(data.getMember_name()) && this.user.getMember_nickname() != null && this.user.getMember_nickname().equalsIgnoreCase(data.getMember_nickname())) {
            return;
        }
        this.user = data;
        binding.setUser(user);
        float radius = DisplayUtil.dip2px(2);
        int width = (int) DisplayUtil.dip2px(40);
        Glide.with(getActivity()).load(user.getMember_avatar()).asBitmap().transform(new RoundTransform(getContext()).setOutsideColor(Color.WHITE).setOutsideWidth(DisplayUtil.dip2px(1)).setLeftBottomRadius(radius).setRightBottomRadius(radius).setLeftTopRadius(radius).setRightTopRadius(radius)).into(new SimpleTarget<Bitmap>(width, width) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int width = (int) DisplayUtil.dip2px(200);
                binding.setBitmap(EncodingUtils.createQRCode("OMI_" + user.getMember_name(), width, width, resource));
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                int width = (int) DisplayUtil.dip2px(200);
                binding.setBitmap(EncodingUtils.createQRCode("OMI_" + user.getMember_name(), width, width, null));
            }
        });
    }

    public User.Data getUser() {
        return user;
    }
}
